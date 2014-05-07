/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chintanrathod.fillpercentageimage;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends Activity {
	Button btnFillIt, btnFillFull;
	ImageView imgHolder;
	Paint paint;
	EditText edtPercentage;
	int percentage;
	CheckBox chkTopToBottom;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		imgHolder = (ImageView) findViewById(R.id.coringImage);
		btnFillIt = (Button) findViewById(R.id.btnFillIt);
		edtPercentage = (EditText) findViewById(R.id.edtPercentage);
		btnFillFull = (Button) findViewById(R.id.btnFillFull);
		chkTopToBottom = (CheckBox) findViewById(R.id.chkTopToBottom);

		paint = new Paint();

		btnFillIt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (edtPercentage.getText().toString().length() > 0) {

					int percentage = Integer.parseInt(edtPercentage.getText()
							.toString());
					imgHolder.setImageResource(R.drawable.petrolpump_simple);

					if (percentage > 0 && percentage < 100) {
						if (chkTopToBottom.isChecked()) {
							imgHolder
									.setImageBitmap(doTopToBottomOperation(percentage));
						} else {
							imgHolder
									.setImageBitmap(doBottomToTopOperation(percentage));
						}
					}
				}
			}
		});

		btnFillFull.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final Handler handler = new Handler();
				percentage = 0;
				imgHolder.setImageResource(R.drawable.petrolpump_simple);

				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						percentage += 2;
						if (chkTopToBottom.isChecked()) {
							imgHolder
									.setImageBitmap(doTopToBottomOperation(percentage));
						} else {
							imgHolder
									.setImageBitmap(doBottomToTopOperation(percentage));
						}

						if (percentage < 100)
							handler.postDelayed(this, 10);
					}
				}, 10);
			}
		});

	}

	public Bitmap doTopToBottomOperation(int percentage) {
		Bitmap bitmapOriginal = ((BitmapDrawable) imgHolder.getDrawable())
				.getBitmap();

		Bitmap bitmapTarget = BitmapFactory.decodeResource(getResources(),
				R.drawable.petrolpump);

		int heightToCrop = bitmapTarget.getHeight() * percentage / 100;

		Bitmap croppedBitmap = Bitmap.createBitmap(bitmapTarget, 0, 0,
				bitmapTarget.getWidth(), heightToCrop);

		Bitmap bmOverlay = Bitmap.createBitmap(bitmapOriginal.getWidth(),
				bitmapOriginal.getHeight(), bitmapOriginal.getConfig());
		Canvas canvas = new Canvas(bmOverlay);
		canvas.drawBitmap(bitmapOriginal, new Matrix(), null);
		canvas.drawBitmap(croppedBitmap, new Matrix(), null);

		return bmOverlay;
	}

	public Bitmap doBottomToTopOperation(int percentage) {
		Bitmap bitmapOriginal = ((BitmapDrawable) imgHolder.getDrawable())
				.getBitmap();

		Bitmap bitmapTarget = BitmapFactory.decodeResource(getResources(),
				R.drawable.petrolpump);

		int heightToCrop = bitmapTarget.getHeight() * (100 - percentage) / 100;

		Bitmap croppedBitmap = Bitmap.createBitmap(bitmapTarget, 0,
				heightToCrop, bitmapTarget.getWidth(), bitmapTarget.getHeight()
						- heightToCrop);

		Bitmap bmOverlay = Bitmap.createBitmap(bitmapOriginal.getWidth(),
				bitmapOriginal.getHeight(), bitmapOriginal.getConfig());
		Canvas canvas = new Canvas(bmOverlay);
		canvas.drawBitmap(bitmapOriginal, new Matrix(), null);
		canvas.drawBitmap(croppedBitmap,
				canvas.getWidth() - croppedBitmap.getWidth(),
				canvas.getHeight() - croppedBitmap.getHeight(), null);

		return bmOverlay;
	}
}
