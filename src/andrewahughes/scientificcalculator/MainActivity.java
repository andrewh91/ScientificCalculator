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
import java.util.List;

public class MainActivity extends Activity 
{
	public enum operatorFlag{none,subtract,plus,divide,multiply,squareRoot,power,bracket};//enumerators for BIDMAS order
	public operatorFlag flag=operatorFlag.none; //initialise 
	public BigDecimal number = new BigDecimal(0), answer=new BigDecimal(0),subtract = new BigDecimal(1);
	public byte decimal=0,integer=1,digitNo=0; 
	public char operator = ' ';
	public class Objects//used for history
	{
		BigDecimal number;
		operatorFlag flag;
		Objects(BigDecimal n ,operatorFlag f) 
		{
			number=n;
			flag=f;
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
		button[4].setOnLongClickListener(new View.OnLongClickListener() //use subtract operator
		{
			@Override
			public boolean onLongClick(View v) {
				subtract();//subtract operator
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
		button[14].setOnClickListener(new View.OnClickListener() //ans button
		{
			@Override
			public void onClick(View v) {
				ans();//sets current number to previous answer
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
		  text.setText(input+""+operator); //sets text to the number entered, and an operator if one is pressed
	  }
	  public void decimalMode()//switch number entry mode to decimal
	  {
		  decimal=1;//sets values used in appendNumber method
		  integer=0;
		  operator = '.';//shows the decimal point after the number to confirm we are in decimal mode
		  displayNumber(number);//updates display
	  }
	  public void integerMode()//switch number entry mode to integer
	  {
		  integer = 1;//sets integer mode and disable decimal mode
		  decimal = 0;//sets integer mode and disable decimal mode
		  digitNo = 0;//resets the number of decimal places
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
			  objects.add(new Objects(number,operatorFlag.none));//add the current number to a list
		  }
		  numberEntered=false;
	  }
	  public void multiply()//adds current number to memory, sets flag o multiply for equals method
	  {
		  addNumToHistory(number);//adds current number to memory
		  operator = '*';//sets the operator
		  displayNumber(number);//updates the display with the operator
		  if(flag.compareTo(operatorFlag.multiply)<0)//if current flag is of less or equal BIDMAS importance...
		  {
			  flag = operatorFlag.multiply;//...set flag to determine what "=" does when pressed			  
		  }
		  
		  newNumberMode();//make sure the next number we enter is a positive integer by default
	  }
	  public void plus()//adds current number to memory, sets flag to plus so that plus is called during equals method
	  {
		  addNumToHistory(number);//adds current number to memory
		  displayNumber(number);//updates the display with the operator (which is set on the button method, because the subtract button calls this method, and changing the operator here would mean we can't have a minus operator)
		  if(flag.compareTo(operatorFlag.plus)<0)//if current flag is of less or equal BIDMAS importance...
		  {
			  flag = operatorFlag.plus;//...set flag to determine what "=" does when pressed			  
		  }
		  newNumberMode();//make sure the next number we put in is a positive integer by default
	  }
	  public void subtract()//calls plus in case the intention is subtraction, also sets next number to negative sign
	  {
		  operator ='-';//appends - operator to confirm button was pressed
		  plus();//call plus because subtraction is the same a addition but with negative numbers
		  subtract = new BigDecimal(-1);//this sets the subtract variable which affects the appendNumber method
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
	  public void ans()//answer button, sets current number to last answer
	  {
		  number = answer;//simply add current number to previous answer
		  displayNumber(number);//update display
	  }
	  public void equalsOp()//method which performs calculations
	  {
		  switch (flag)//detect what current flag is 
		  {
			  case none: ;//if none, no calculation has been pressed, = has probably been pressed in error
			  break;//break the statement, no need to carry on as the following cases can not be true
			  case multiply: addNumToHistory(number); answer = objects.get(objects.size()-2).number.multiply(objects.get(objects.size()-1).number);
			  break;//add current number to history, multiply the 2nd to last number entered with the last number entered
			  case plus: addNumToHistory(number); answer = objects.get(objects.size()-2).number.add(objects.get(objects.size()-1).number); 
			  break;//add current number to history, add the 2nd to last number entered with the last number entered
			  default: 
			  break;
			  
		  }
		  reset();//reset ready for a new calculation
		  displayNumber(answer);//update the display with the answer
	  }
}