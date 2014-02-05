package andrewahughes.scientificcalculator;

import andrewahughes.scientificcalculator.R.id;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity 
{
	public enum operatorFlag{plus,subtract,multiply,divide,power,squareRoot};
	public operatorFlag flag; 
	public double number = 0,number1=0;
	public byte decimal=0; 
	public byte integer=1; 
	public byte digitNo=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button[] button = new Button[16];
		button[0]= (Button) findViewById(id.button42);//0
		button[1]= (Button) findViewById(id.button11);//1
		button[2]= (Button) findViewById(id.button12);//2
		button[3]= (Button) findViewById(id.button13);//3
		button[4]= (Button) findViewById(id.button21);//4
		button[5]= (Button) findViewById(id.button22);//5
		button[6]= (Button) findViewById(id.button23);//6
		button[7]= (Button) findViewById(id.button31);//7
		button[8]= (Button) findViewById(id.button32);//8
		button[9]= (Button) findViewById(id.button33);//9
		button[10]= (Button) findViewById(id.button14);//a
		button[11]= (Button) findViewById(id.button24);//b
		button[12]= (Button) findViewById(id.button34);//c
		button[13]= (Button) findViewById(id.button41);//d
		button[14]= (Button) findViewById(id.button43);//e
		button[15]= (Button) findViewById(id.button44);//f
		for (int i = 0; i < 10; i++)
		{
			final int input = i;
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
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	  public void appendNumber(View view,int input)//used to enter digits one after another in a traditional calculator fashion
	  {
			TextView text = (TextView) findViewById(id.displayText);
			number=(number*Math.pow(10, integer))+input*(Math.pow(10,-decimal*(digitNo+1)));
			digitNo= (byte) (digitNo+(1*decimal));
			text.setText(""+number);  
	  }
	  public void decimalMode()
	  {
		  decimal=1;
		  integer=0;
	  }
	  public void integerMode()
	  {
		  integer =1;
		  decimal=0;
	  }
	  public void plus(View view,double a)
	  {
		  number1=a;
		  flag = operatorFlag.plus;
	  }
}