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

public class MainActivity extends Activity 
{
	public enum operatorFlag{none,plus,subtract,multiply,divide,power,squareRoot};
	public operatorFlag flag=operatorFlag.none; 
	public BigDecimal number = new BigDecimal(0),number1=new BigDecimal(0), answer=new BigDecimal(0);
	public byte decimal=0; 
	public byte integer=1; 
	public byte digitNo=0;
	public BigDecimal subtract = new BigDecimal(1);
	public char operator = ' ';
	
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
				operator ='+';
				plus(number);
				return true;
			}
		});
		button[4].setOnLongClickListener(new View.OnLongClickListener() //use subtract operator
		{
			@Override
			public boolean onLongClick(View v) {
				subtract();
				return true;
			}
		});
		button[9].setOnLongClickListener(new View.OnLongClickListener() //use equals operator
		{
			@Override
			public boolean onLongClick(View v) {
				equalsOp();
				return true;
			}
		});
		button[10].setOnClickListener(new View.OnClickListener() //use equals operator
		{
			@Override
			public void onClick(View v) {
				backSpace();//deletes last digit entered
			}
		});
		button[10].setOnLongClickListener(new View.OnLongClickListener() //use equals operator
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
	  {
			number=(number.multiply(BigDecimal.TEN.pow(integer))).add((input.multiply(subtract)).multiply(BigDecimal.TEN.pow(-decimal*(digitNo+1),MathContext.DECIMAL64)));
			digitNo= (byte) (digitNo+(1*decimal));
			operator=' ';
			displayNumber(number);
	  }
	  public void displayNumber(BigDecimal input)
	  {
		  TextView text = (TextView) findViewById(id.displayText);
		  text.setText(input+""+operator); 
	  }
	  public void decimalMode()//switch number entry mode to decimal
	  {
		  decimal=1;
		  integer=0;
		  operator = '.';
		  displayNumber(number);
	  }
	  public void integerMode()
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
		  newNumberMode();
		  operator = ' ';//resets the operator symbol
		  number1=new BigDecimal(0); //resets number in memory 
	  }
	  public void plus(BigDecimal input)//don't really need an argument, my variables are public anyway
	  {
		  number1=number1.add(input);//adds current number to number in memory
		  displayNumber(number);//updates the display with the operator 
		  flag = operatorFlag.plus;//sets flag to determine what "=" does when pressed
		  newNumberMode();//make sure the next number we put in is a positive integer by default
	  }
	  public void subtract()
	  {
		  operator ='-';
		  plus(number);
		  subtract = new BigDecimal(-1);
	  }
	  public void backSpace()//undos the last number entered
	  {
		  number = (number.divide(BigDecimal.TEN.pow(integer)));//divide by 10 if integer
		  if(digitNo>1)
		  {
			  digitNo--;
		  }
		  else
		  {
			  integerMode();
			  operator=' ';
		  }
		  number = number.setScale(digitNo, BigDecimal.ROUND_DOWN);//round towards zero, digitNo controls decimal places to round to
		  displayNumber(number);
	  }
	  public void clear()//clears display
	  {
		  newNumberMode();
		  operator = ' ';//resets the operator symbol
		  displayNumber(number);
		  flag =operatorFlag.none;
	  }
	  public void equalsOp()
	  {
		  switch (flag)
		  {
			  case none: reset();
			  break;
			  case plus: answer = number1.add(number); reset();
			  break;
			  default: 
			  break;
			  
		  }
		  displayNumber(answer);
	  }
	  public void ans()//answer button, sets current number to last answer
	  {
		  number = answer;
		  displayNumber(number);
	  }
}