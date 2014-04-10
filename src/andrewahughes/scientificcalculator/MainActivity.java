package andrewahughes.scientificcalculator;

import andrewahughes.scientificcalculator.R.id;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends Activity 
{
	public enum operatorFlag{none,subtract,plus,divide,multiply,squareRoot,power,sqrt,bracket};//enumerators for BIDMAS order
	public operatorFlag flag=operatorFlag.none; //initialise
	public operatorFlag currentOperatorFlag= operatorFlag.none;//used to set operator flag of number as soon as you enter it
	public BigDecimal number = new BigDecimal(0), answer=new BigDecimal(0),subtract = new BigDecimal(1),memoryStore=new BigDecimal(0);
	public byte decimal=0,integer=1,digitNo=0; 
	public char operator = ' ',inverseChar=' ',hyperbolicChar=' ';
	public boolean inverse=false,hyperbolic=false;
	double d;//used in trig
	int currentBracket=0,highestBracket=0, noOfNumbers=0;
	
	public class Objects//used for storing numbers, and their properties
	{
		BigDecimal number;
		operatorFlag operator1;
		int bracketNo;
		boolean remove=false;
		Objects(BigDecimal n) 
		{
			number=n;
			bracketNo= currentBracket;
			
		}
	}
	List<Objects> objects =new ArrayList<Objects>();//used to store numbers and calculations in memory
	public boolean numberEntered = false;//fix bug where 0 was recorded as current number if you press two operators
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button[] button = new Button[16];
		button[0]= (Button) findViewById(id.button42);//0	[4,2]
		button[1]= (Button) findViewById(id.button11);//1	[1,1]
		button[2]= (Button) findViewById(id.button12);//2	[1,2]
		button[3]= (Button) findViewById(id.button13);//3	[1,3]
		button[4]= (Button) findViewById(id.button21);//4	[2,1]
		button[5]= (Button) findViewById(id.button22);//5	[2,2]
		button[6]= (Button) findViewById(id.button23);//6	[2,3]
		button[7]= (Button) findViewById(id.button31);//7	[3,1]
		button[8]= (Button) findViewById(id.button32);//8	[3,2]
		button[9]= (Button) findViewById(id.button33);//9	[3,3]
		button[10]= (Button) findViewById(id.button14);//a	[1,4]
		button[11]= (Button) findViewById(id.button24);//b	[2,4]
		button[12]= (Button) findViewById(id.button34);//c	[3,4]
		button[13]= (Button) findViewById(id.button41);//d	[4,1]
		button[14]= (Button) findViewById(id.button43);//e	[4,3]
		button[15]= (Button) findViewById(id.button44);//f	[4,4]
		for (int i = 0; i < 10; i++)
		{
			final BigDecimal input = new BigDecimal(i);
			button[i].setOnClickListener(new View.OnClickListener() 
			{
				public void onClick(View view)
				{
					appendNumber(view,input);
				}
			});
		}
		button[0].setOnLongClickListener(new View.OnLongClickListener() //enter decimalMode, enters a decimal point
		{
			@Override
			public boolean onLongClick(View view) 
			{
				decimalMode();
				return true;
			}
		});
		button[1].setOnLongClickListener(new View.OnLongClickListener() //use plus operator
		{
			@Override
			public boolean onLongClick(View v) {
				subtract=new BigDecimal(1);//resets subtract, need in case we add or subtract multiple numbers in a row, e.g. 1-2+3+4=
				operator ='+';//appends operator here because subtract shares a method with plus, and putting this in the method would overwrite the - 
				addNumToHistory(number);//adds current number to memory
				setOperatorFlag(operatorFlag.plus);//set flag here, because subtract uses the same method but different code involving the flag
				plus();//plus operator
				return true;
			}
		});
		button[2].setOnLongClickListener(new View.OnLongClickListener() //use multiply operator
		{
			@Override
			public boolean onLongClick(View v) {
				multiply();//multiply operator
				return true;
			}
		});
		button[3].setOnLongClickListener(new View.OnLongClickListener() //use power operator
		{
			@Override
			public boolean onLongClick(View v) {
				power();//power operator
				return true;
			}
		});
		button[4].setOnLongClickListener(new View.OnLongClickListener() //use subtract operator
		{
			@Override
			public boolean onLongClick(View v) {
				addNumToHistory(number);//adds current number to memory
				setOperatorFlag(operatorFlag.plus);//
				subtract();//subtract operator
				return true;
			}
		});
		button[5].setOnLongClickListener(new View.OnLongClickListener() //use divide operator
		{
			@Override
			public boolean onLongClick(View v) {
				divide();//divide operator
				return true;
			}
		});
		button[6].setOnLongClickListener(new View.OnLongClickListener() //use root operator
		{
			@Override
			public boolean onLongClick(View v) {
				root();//root operator
				return true;
			}
		});
		button[7].setOnLongClickListener(new View.OnLongClickListener() //use root operator
		{
			@Override
			public boolean onLongClick(View v) {
				openBracket();//open bracket operator
				return true;
			}
		});
		button[8].setOnLongClickListener(new View.OnLongClickListener() //use root operator
		{
			@Override
			public boolean onLongClick(View v) {
				closeBracket();//close bracket operator
				return true;
			}
		});
		button[9].setOnLongClickListener(new View.OnLongClickListener() //use equals operator
		{
			@Override
			public boolean onLongClick(View v) {
				equalsOp();//equals operator
				return true;
			}
		});
		button[10].setOnClickListener(new View.OnClickListener() //use backspace operator
		{
			@Override
			public void onClick(View v) {
				backSpace();//deletes last digit entered
			}
		});
		button[10].setOnLongClickListener(new View.OnLongClickListener() //use clear operator
		{
			@Override
			public boolean onLongClick(View v) {
				clear();//clears display
				return true;
			}
		});
		button[11].setOnClickListener(new View.OnClickListener() //use sine operator
		{
			@Override
			public void onClick(View v) {
				sin();
			}
		});
		button[11].setOnLongClickListener(new View.OnLongClickListener() //use tangent operator
		{
			@Override
			public boolean onLongClick(View v) {
				tan();
				return true;
			}
		});
		button[12].setOnClickListener(new View.OnClickListener() //use cosine operator
		{
			@Override
			public void onClick(View v) {
				cos();
			}
		});
		button[12].setOnLongClickListener(new View.OnLongClickListener() //set inverse 
		{
			@Override
			public boolean onLongClick(View v) {
				inv();
				return true;
			}
		});
		button[13].setOnClickListener(new View.OnClickListener() //memory recall button
		{
			@Override
			public void onClick(View v) {
				mr();//sets current number to stored number
			}
		});
		button[13].setOnLongClickListener(new View.OnLongClickListener() //memory store button
		{
			@Override
			public boolean onLongClick(View v) {
				ms();//sets stored number to current number
				return true;
			}
		});
		button[14].setOnClickListener(new View.OnClickListener() //ans button
		{
			@Override
			public void onClick(View v) {
				ans();//sets current number to previous answer
			}
		});
		button[15].setOnClickListener(new View.OnClickListener() //PI button
		{
			@Override
			public void onClick(View v) {
				pi();//sets current number to PI
			}
		});
		button[15].setOnLongClickListener(new View.OnLongClickListener() //sets hyperbolic
		{
			@Override
			public boolean onLongClick(View v) {
				hyp();
				return true;
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	  public void appendNumber(View view,BigDecimal input)//used to enter digits one after another in a traditional calculator fashion
	  {	//the following calculation appends positive and negative, integer and decimal numbers as they are entered 
			number=(number.multiply(BigDecimal.TEN.pow(integer))).add((input.multiply(subtract)).multiply(BigDecimal.TEN.pow(-decimal*(digitNo+1),MathContext.DECIMAL64)));
			digitNo= (byte) (digitNo+(1*decimal));//keeps track of decimal places etc
			operator=' ';//clears operator from previous display
			displayNumber(number);//updates the display
			numberEntered = true;//record the fact that the most recent action was a number being entered
	
	  }
	  public void displayNumber(BigDecimal input)//updates the display, with supplied argument, could be current number or answer
	  {
		  TextView text = (TextView) findViewById(id.displayText);//text view at the top of the screen
		  text.setText(input+""+operator+inverseChar+hyperbolicChar); //sets text to the number entered, and an operator if one is pressed
	  }
	  public void decimalMode()//switch number entry mode to decimal
	  {
		  if(flag.compareTo(operatorFlag.power)!=0)//if current flag is NOT power, set decimal mode (prevents decimal exponent )
		  {
			  decimal=1;//sets values used in appendNumber method
			  integer=0;
			  operator = '.';//shows the decimal point after the number to confirm we are in decimal mode
			  displayNumber(number);//updates display
		  }
	  }
	  public void integerMode()//switch number entry mode to integer
	  {
		  integer = 1;//sets integer mode and disable decimal mode
		  decimal = 0;//sets integer mode and disable decimal mode
		  digitNo = 0;//resets the number of decimal places
		  inverse=false;
		  hyperbolic=false;
		  hyperbolicChar=' ';
		  inverseChar=' ';
	  }
	  public void newNumberMode()//set number entry mode to integer, set some other defaults too
	  {
		  integerMode();
		  number = new BigDecimal(0); //resets the value of the number entered
		  subtract = new BigDecimal(1);//resets the sign of the next number
	  }
	  public void reset()//set number entry mode to integer, set some other defaults too
	  {
		  newNumberMode();//sets defaults to prepare for a new number,  
		  operator = ' ';//resets the operator symbol
		  flag = operatorFlag.none;//resets the operator flag, ready for a new calculation 
	  }
	  public void addNumToHistory(BigDecimal number)//adds current number to history list
	  {
		  if(numberEntered)	//if the last thing entered is a number i.e. not an operator
		  {
			  objects.add(new Objects(number));//add the current number to a list, with the current operator flag as one of it's flags
		  }
		  numberEntered=false;
	  }
	  void setOperatorFlag(operatorFlag newOperatorFlag)// a method to set the current number's flag to the operator being pressed
	  {
		  currentOperatorFlag=newOperatorFlag;
		  objects.get(objects.size()-1).operator1= newOperatorFlag;
		  objects.get(objects.size()-1).bracketNo=currentBracket;
	  }
	  public void root()
	  {
		  addNumToHistory(number);//adds current number to memory
		  operator='r';//sets the operator
		  displayNumber(number);//updates the display with the operator
		  setOperatorFlag(operatorFlag.sqrt);
		  /*if(flag.compareTo(operatorFlag.sqrt)<0)//if current flag is of less or equal BIDMAS importance...
		  {
			  flag = operatorFlag.sqrt;//...set flag to determine what "=" does when pressed	
		  }*/
		  newNumberMode();//make sure the next number we enter is a positive integer by default
	  }
	  public void power()//adds current number to memory, sets flag to power, so the equals method will raise the initial number to the power of the second in memory
	  {
		  addNumToHistory(number);//adds current number to memory
		  operator='^';//sets the operator
		  displayNumber(number);//updates the display with the operator
		  setOperatorFlag(operatorFlag.power);
		  /*if(flag.compareTo(operatorFlag.power)<0)//if current flag is of less or equal BIDMAS importance...
		  {
			  flag = operatorFlag.power;//...set flag to determine what "=" does when pressed	
		  }*/
		  newNumberMode();//make sure the next number we enter is a positive integer by default
	  }
	  public void divide()//adds current number to memory, sets flag to divide, so the equals method will divide the numbers in memory
	  {
		  addNumToHistory(number);//adds current number to memory
		  operator='/';//sets the operator
		  displayNumber(number);//updates the display with the operator
		  setOperatorFlag(operatorFlag.divide);
		  /*if(flag.compareTo(operatorFlag.divide)<0)//if current flag is of less or equal BIDMAS importance...
		  {
			  flag = operatorFlag.divide;//...set flag to determine what "=" does when pressed	
		  }*/
		  newNumberMode();//make sure the next number we enter is a positive integer by default
	  }
	  public void multiply()//adds current number to memory, sets flag o multiply for equals method
	  {
		  addNumToHistory(number);//adds current number to memory
		  operator = '*';//sets the operator
		  displayNumber(number);//updates the display with the operator
		  setOperatorFlag(operatorFlag.multiply);
		  /*if(flag.compareTo(operatorFlag.multiply)<0)//if current flag is of less or equal BIDMAS importance...
		  {
			  flag = operatorFlag.multiply;//...set flag to determine what "=" does when pressed			  
		  }*/
		  newNumberMode();//make sure the next number we enter is a positive integer by default
	  }
	  public void plus()//adds current number to memory, sets flag to plus so that plus is called during equals method
	  {
		  displayNumber(number);//updates the display with the operator (which is set on the button method, because the subtract button calls this method, and changing the operator here would mean we can't have a minus operator)
		  /*
		  if(flag.compareTo(operatorFlag.plus)<0)//if current flag is of less or equal BIDMAS importance...
		  {
			  flag = operatorFlag.plus;//...set flag to determine what "=" does when pressed			  
		  }*/
		  newNumberMode();//make sure the next number we put in is a positive integer by default
	  }
	  public void subtract()//calls plus in case the intention is subtraction, also sets next number to negative sign
	  {
		  operator ='-';//appends - operator to confirm button was pressed
		  plus();//call plus because subtraction is the same a addition but with negative numbers
		  if(objects.get(objects.size()-1).operator1==operatorFlag.none)//if we use an operator in conjunction with...  
		  {																//...negative numbers, the minus replaces the operator flag...
			  objects.get(objects.size()-1).operator1= operatorFlag.plus;//...this makes sure we only change the flag if the flag  wasn't already set
		  }
		  //subtract = new BigDecimal(-1);//this sets the subtract variable which affects the appendNumber method
		  numberEntered=true;
		  addNumToHistory(new BigDecimal(-1));
		  objects.get(objects.size()-1).operator1= operatorFlag.multiply;
	  }
	  public void backSpace()//undoes the last number entered
	  {
		  number = (number.divide(BigDecimal.TEN.pow(integer)));//divide number by 10 if integer
		  if(digitNo>1)//if number of decimal places is more than 1 ...
		  {
			  digitNo--;//...reduce the number of decimal places
		  }
		  else//if decimal places <=1 when backspace is pushed...
		  {
			  integerMode();//...then we have to change from decimal to integer mode 
			  operator=' ';//clear the decimal place operator to show we have passed back into integer mode
		  }
		  number = number.setScale(digitNo, BigDecimal.ROUND_DOWN);//round towards 0 to clean up after we divided by 10 earlier
		  displayNumber(number);//updates display
	  }
	  public void clear()//clears display
	  {
		  newNumberMode();//prepare for  new number to be entered
		  operator = ' ';//resets the operator symbol
		  displayNumber(number);//updates display
		  flag =operatorFlag.none;//resets operator flag ready for a new calculation 
	  }
	  public void sin()//uses the sin, arcsin or sinh trig function according to the value of the inverse and hyperbolic flags
	  {
		  d=number.doubleValue();
		  if(!inverse&&!hyperbolic)
		  {
			  d=Math.sin(d);
		  }
		  else if(inverse&&!hyperbolic)
		  {
			  d=Math.asin(d);
		  }
		  else if(!inverse&&hyperbolic)
		  {
			  d=Math.sinh(d);
		  }
		  else //if(inverse&&hyperbolic);
		  {
			  //make an inverse hyperbolic function
		  }
		  number=new BigDecimal(d);
		  answer=number;//sets answer to number so we can use ans button to carry on calculation
		  integerMode();//clear the inverse or hyperbolic char if present
		  displayNumber(number);
		  numberEntered=true;
		  newNumberMode();
	  }
	  public void cos()//uses the cos, arccos or cosh trig function according to the value of the inverse and hyperbolic flags
	  {
		  d=number.doubleValue();
		  if(!inverse&&!hyperbolic)
		  {
			  d=Math.cos(d);
		  }
		  else if(inverse&&!hyperbolic)
		  {
			  d=Math.acos(d);
		  }
		  else if(!inverse&&hyperbolic)
		  {
			  d=Math.cosh(d);
		  }
		  else //if(inverse&&hyperbolic);
		  {
			  //make an inverse hyperbolic function
		  }
		  number=new BigDecimal(d);
		  answer=number;//sets answer to number so we can use ans button to carry on calculation
		  integerMode();//clear the inverse or hyperbolic char if present
		  displayNumber(number);
		  numberEntered=true;
		  newNumberMode();
	  }
	  public void tan()//uses the tan, arctan or tanh trig function according to the value of the inverse and hyperbolic flags
	  {
		  d=number.doubleValue();
		  if(!inverse&&!hyperbolic)
		  {
			  d=Math.tan(d);
		  }
		  else if(inverse&&!hyperbolic)
		  {
			  d=Math.atan(d);
		  }
		  else if(!inverse&&hyperbolic)
		  {
			  d=Math.tanh(d);
		  }
		  else //if(inverse&&hyperbolic);
		  {
			  //make an inverse hyperbolic function
		  }
		  number=new BigDecimal(d);
		  answer=number;//sets answer to number so we can use ans button to carry on calculation
		  integerMode();//clear the inverse or hyperbolic char if present
		  displayNumber(number);
		  numberEntered=true;
		  newNumberMode();
	  }
	  public void inv()//sets the inverse flag to determine which trig function we use
	  {
		  if(inverse)
		  {
			  inverse=false;
			  inverseChar=' ';
		  }
		  else
		  {
			  inverse=true;
			  inverseChar='i';
		  }
		  displayNumber(number);
	  }
	  public void pi()
	  {
		  number = subtract.multiply(new BigDecimal(Math.PI));//equate the current number to PI, or minus PI
		  answer=number;//sets answer to number so we can use ans button to carry on calculation
		  displayNumber(number);//update display
		  numberEntered=true;
	  }
	  public void hyp()//sets the hyperbolic flag to determine which trig function we use
	  {
		  if(hyperbolic)
		  {
			  hyperbolic=false;
			  hyperbolicChar=' ';
		  }
		  else
		  {
			  hyperbolic=true;
			  hyperbolicChar='h';
		  }
		  displayNumber(number);
	  }
	  public void ans()//answer button, sets current number to last answer
	  {
		  number = answer.multiply(subtract);//simply add current number to previous answer
		  displayNumber(number);//update display
		  numberEntered=true;
	  }
	  public void his()
	  {
		  
	  }
	  public void ms()//memory store button, sets stored number to current number
	  {
		  memoryStore = number;//store current number to variable
		  displayNumber(number);//update display
	  }
	  public void mr()//memory recall button, sets current number to stored number
	  {
		  number = memoryStore.multiply(subtract);//set current number to stored variable
		  displayNumber(number);//update display
		  numberEntered=true;
	  }
	  public void openBracket(){
		  highestBracket++;
		  currentBracket++;
		  operator ='(';
		  displayNumber(number);
		 
	  }
	  public void closeBracket(){
		  currentBracket--;
		  operator =')';
		  displayNumber(number);
	  }
	  public void removeNumber()
	  {
		  for(Iterator<Objects> itr = objects.iterator();itr.hasNext();)
		  {
			  if(itr.next().remove)
			  {
				  itr.remove();
				  noOfNumbers --;
			  }
		  }
	  }
	  public void equalsOp()//method which performs calculations
	  {
		  addNumToHistory(number);
		  //TODO brackets
		  noOfNumbers=objects.size(); 
		  for(int j=highestBracket;j>=0;j--)
		  {
			  for(int i = 0;i<noOfNumbers;i++)
			  {
				  if(objects.get(i).operator1==operatorFlag.power&&objects.get(i).bracketNo==j)
				  {
					  	answer= objects.get(i).number.pow(objects.get(i+1).number.intValue(),MathContext.DECIMAL64);
					  	objects.get(i+1).number=answer;
					  	//objects.get(i).operator2=objects.get(i+1).operator2;
					  	objects.get(i).remove=true;
				  }
			  }
			  removeNumber();
			  for(int i = 0;i<noOfNumbers;i++)
			  {
				  if(objects.get(i).operator1==operatorFlag.divide&&objects.get(i).bracketNo==j)
				  {
					  	answer= objects.get(i).number.divide(objects.get(i+1).number,MathContext.DECIMAL64);
					  	objects.get(i+1).number=answer;
					  	//objects.get(i).operator2=objects.get(i+1).operator2;
					  	objects.get(i).remove=true;
				  }
			  }
			  removeNumber();
			  for(int i = 0;i<noOfNumbers;i++)
			  {
				  if(objects.get(i).operator1==operatorFlag.multiply&&objects.get(i).bracketNo==j)
				  {
					  	answer= objects.get(i).number.multiply(objects.get(i+1).number,MathContext.DECIMAL64);
					  	objects.get(i+1).number=answer;
					  	//objects.get(i).operator2=objects.get(i+1).operator2;
					  	objects.get(i).remove=true;
				  }
			  }
			  removeNumber();
			  for(int i = 0;i<noOfNumbers;i++)
			  {
				  if(objects.get(i).operator1==operatorFlag.plus&&objects.get(i).bracketNo==j)
				  {
					  	answer= objects.get(i).number.add(objects.get(i+1).number,MathContext.DECIMAL64);
					  	objects.get(i+1).number=answer;
					  	//objects.get(i).operator2=objects.get(i+1).operator2;
					  	objects.get(i).remove=true;
				  }
			  }
			  removeNumber();

		  }
		  reset();//reset ready for a new calculation
		  displayNumber(answer);//update the display with the answer
	  }
}