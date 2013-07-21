package com.webwayz.ratingcalc;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
//import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import java.io.*;

public class MainActivity extends Activity {
	private EditText text;
	private EditText opponentText;
	private EditText txtData;
    public String gameResult, oppRating, oldRating, newRating;
	private Button writeButton, readButton;
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_main);
	  text = (EditText) findViewById(R.id.EditText01);
	  opponentText = (EditText) findViewById(R.id.EditText02);
	  gameResult ="";
	  newRating = "";
	  writeButton = (Button)findViewById(R.id.WriteButton);
	  writeButton.setEnabled(false);
	  readButton = (Button)findViewById(R.id.ListButton);
	  readButton.setEnabled(false);
	 }

	 // This method is called at button click because we assigned the name to the
	 // "On Click property" of the button
	 public void buttonHandler(View view) {
		
		 writeButton.setEnabled(false);
	  switch (view.getId()) {
	  case R.id.CalcButton:
	   RadioButton winButton = (RadioButton) findViewById(R.id.RadioButton01);
	   RadioButton lossButton = (RadioButton) findViewById(R.id.RadioButton03);
	   RadioButton drawButton = (RadioButton) findViewById(R.id.RadioButton02);
	   if (text.getText().length() == 0) {
	    Toast.makeText(
	      this,
	      "Please enter a valid number", Toast.LENGTH_LONG).show();
	    return;
	   }
	   if (opponentText.getText().length() == 0) {
		    Toast.makeText(
		      this,
		      "Please enter a valid number", Toast.LENGTH_LONG).show();
		    return;
		   }  
	   float inputValue = Float.parseFloat(text.getText().toString());
	   float opponentValue = Float.parseFloat(opponentText.getText().toString());
		
	   if (inputValue <700 || opponentValue < 700) {
		    Toast.makeText(
		      this,
		      "Please enter a valid Rating", Toast.LENGTH_LONG).show();
		    return;
		   } else if (inputValue >3000 || opponentValue > 3000 ) {
			    Toast.makeText(
					      this, inputValue +
					      " In your Dreams! Please enter a valid Rating", Toast.LENGTH_LONG).show();
					    return;
					   }
				  
	   oppRating = opponentText.getText().toString();
	   oldRating = text.getText().toString();
	   float diff = 1450 - 1650;
	   diff = opponentValue - inputValue;
	   
	   if (winButton.isChecked()) {
		   gameResult ="Win";
		   text.setText(removeDecimalPart(ratingChangeWin(diff,inputValue)));
		   newRating = text.getText().toString();
	   } else if (drawButton.isChecked()) {
		    gameResult ="Draw";
		    text.setText(removeDecimalPart(ratingChangeDraw(diff, inputValue)));
		    newRating = text.getText().toString();
	   } else {
		    gameResult = "Loss";
		    text.setText(removeDecimalPart(ratingChangeLoss(diff, inputValue)));
	    	newRating = text.getText().toString();
	   }
	   // Switch to the other button
	   if (lossButton.isChecked()) {
	    lossButton.setChecked(false);
	    drawButton.setChecked(false);
	    winButton.setChecked(true);
	   } else if (winButton.isChecked()){
	    lossButton.setChecked(true);
	    winButton.setChecked(false);
	    drawButton.setChecked(false);
	   } else {
		    lossButton.setChecked(false);
		    winButton.setChecked(false);
		    drawButton.setChecked(true);
	   }
	   //writeButton.setEnabled(true);
	  
	   writeButton.setEnabled(true);
	   
	   break;
	   
	  case R.id.WriteButton:
		  try {
			  //File myFile = new File(getString(R.string.outPutFile));
			  //myFile.createNewFile();
			  //FileOutputStream fOut= new FileOutputStream(myFile);
			  //OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			  //String st = oldRating + "," + oppRating + "," + gameResult +","+ newRating+",";
			  //myOutWriter.append(st.subSequence(1, (st.length()-1)));
		
			  //myOutWriter.close();
			  //fOut.close();
			  File root = Environment.getExternalStorageDirectory();
			  if (root.canWrite()){
				  File gpxfile = new File(root, "chessgames.gpx");
				  FileWriter gpxwriter = null;
				  if (!gpxfile.exists()){
					  gpxfile.createNewFile();
				  }
					gpxwriter =	  new FileWriter(gpxfile, true);
				  String st = oldRating + ", "  + oppRating +" , " +  gameResult + "," + newRating.toString() +"\r\n";
				  BufferedWriter out = new BufferedWriter(gpxwriter);
				  out.append(st);
				  out.close();
				Toast.makeText(getBaseContext(), "Game written to SD Card 'chessgames.txt'", Toast.LENGTH_SHORT).show();
			  	readButton.setEnabled(true); // file written so list games
			  	writeButton.setEnabled(false); //calculate another game to write to file
			  	txtData.setText('W');
			  } else {
				  Toast.makeText(getBaseContext(), "Can't Write Permissions issue", Toast.LENGTH_SHORT).show();}
			} catch (Exception e) {
			  Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
		  }
		  break;
	
	  case R.id.ListButton:
		  try {
				//File myFile = new File(getString(R.string.outPutFile));
			 File myFile = new File(getString(R.string._sdcard_chessgames_gpx));
			  FileInputStream fIn = new FileInputStream(myFile);
				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}
				txtData.setText(aBuffer);
				myReader.close();
				Toast.makeText(getBaseContext(),
						"Done reading SD 'mysdfile.txt'",
						Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				Toast.makeText(getBaseContext(), e.getMessage(),
						Toast.LENGTH_SHORT).show();
			}
	  break;
	  }
	 
	 
	 }

	 // Converts to win
	 private float ratingChangeWin(float diff, float yourRating) {
      float temp = ((diff / 100) * 4) + 16;
      int itemp =0;
      itemp = Math.round(temp);
      temp = (float) itemp;
      if (temp > 0) {
      return temp + yourRating;
      } else 
    	  return yourRating;
      
	 }
	 
	 // Converts to draw
	 private float ratingChangeDraw(float diff, float yourRating) {
	  float newRating =  (((diff / 100) * 4) + yourRating);
      int itemp = Math.round(newRating);
      newRating = (float) itemp;
      return newRating;
	 }
	 // Converts to loss
	 private float ratingChangeLoss(float diff, float yourRating) {
	  float temp = (((diff / 100) * 4) - 16);
      int itemp = (int) Math.round(temp);
      temp = (float) itemp;
	  
	  if (temp < 0) {
	      return temp + yourRating;
	      } else 
	    	  return yourRating;
	    
	 }
	 // get rid of the zeros and decimal point 
	 private static String removeDecimalPart(float f) {
	 String s = String.valueOf(f).replaceAll("[0]*$", "");
	 return s.replace(".", "");
		
	 }
	
	 
	}