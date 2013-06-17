package ru.ifmo.rain.tkachenko.calculator;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeSet;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	Button go;
	TextView result;
	EditText editText;
	LinearLayout history;

	boolean notParseString = false;
	boolean nullDivString = false;

	char[] sign = { '+', '-', '*', '/' };
	String[] signStrings = { "+", "-", "*", "/" };

	static public final String NOT_PARSE = "Can't parse the expression";
	static public final String DIV_NULL = "Division by zero";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);
		go = (Button) findViewById(R.id.Go);
		result = (TextView) findViewById(R.id.answer);
		editText = (EditText) findViewById(R.id.editText);
		history = (LinearLayout) findViewById(R.id.history);

		go.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.Go:
			if (editText.getText().toString().equals("")) {
				break;
			}
			result.setText(getResult(editText.getText().toString()));
			TextView last = new TextView(getApplicationContext());
			last.setText(editText.getText().toString());
			last.setTextSize(31);

			history.addView(last, 0);
			last.setOnClickListener(this);
			editText.setText("");
			notParseString = nullDivString = false;
			break;
		default:
			editText.setText(((TextView) arg0).getText());
			break;
		}
	}

	public String getResult(String s) {
		int curBal = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '(') {
				curBal++;
				if (i > 0 && s.charAt(i - 1) == ')') {
					return NOT_PARSE;
				}
				if (i > 0 && s.charAt(i - 1) != '('
						&& !isOperator(s.charAt(i - 1))) {
					return NOT_PARSE;
				}

			}
			if (s.charAt(i) == ')') {
				if (curBal-- == 0) {
					return NOT_PARSE;
				}
				if (i > 0 && s.charAt(i - 1) == '(') {
					return NOT_PARSE;
				}

				if (i < s.length() - 1 && s.charAt(i + 1) != ')'
						&& !isOperator(s.charAt(i + 1))) {
					return NOT_PARSE;
				}
			}
		}
		if (curBal != 0) {
			return NOT_PARSE;
		}
		String data = "(" + s + ")";
		if (haveTwoOperatorsInRow(s)) {
			return NOT_PARSE;
		}

		String ans = parseExpression(data).toString();
		if (nullDivString) {
			return DIV_NULL;
		}
		if (notParseString) {
			return NOT_PARSE;
		}

		return ans;
	}

	public boolean haveTwoOperatorsInRow(String s) {
		for (int i = 0; i < s.length() - 1; i++) {
			if (isOperator(s.charAt(i)) && isOperator(s.charAt(i + 1))) {
				return true;
			}
		}
		return false;
	}

	public boolean isOperator(char c) {
		for (int i = 0; i < sign.length; i++) {
			if (c == sign[i]) {
				return true;
			}
		}
		return false;
	}

	public Double parseExpression(String data) {
		for (int i = 0; i < data.length(); i++) {
			if (data.charAt(i) == ')') {
				for (int j = i - 1; j >= 0; j--) {
					if (data.charAt(j) == '(') {
						if (j == 0) {
							return parseExpression(data.substring(1,
									data.length() - 1));
						}
						if (data.charAt(j - 1) == '-') {
							Double tmp = -parseExpression(data.substring(j + 1,
									i));
							if (tmp >= 0) {
								return parseExpression(data.substring(0, j - 1)
										+ "+" + tmp.toString()
										+ data.substring(i + 1, data.length()));
							}
							return parseExpression(data.substring(0, j - 1)
									+ tmp.toString()
									+ data.substring(i + 1, data.length()));
						}
						return parseExpression(data.substring(0, j)
								+ parseExpression(data.substring(j + 1, i))
										.toString()
								+ data.substring(i + 1, data.length()));

					}
				}
			}
		}
		return parseSimpleExpression(data);
	}

	public Double parseSimpleExpression(String s) {
		LinkedList<String> expr = new LinkedList<String>();

		if (s.charAt(0) == '-') {
			s = "0" + s;
		}
		if (s.charAt(0) == '+') {
			s = "0" + s;
		}

		String cur = "";

		loop: for (int i = 0; i < s.length(); i++) {
			for (int j = 0; j < sign.length; j++) {
				if (sign[j] == s.charAt(i)) {
					if (sign[j] == '-' && i > 0 && s.charAt(i - 1) == 'E') {
						continue;
					}
					if (cur.equals("")) {
						if (sign[j] == '-') {
							cur = "-";
							continue loop;
						} else {
							notParseString = true;
							return 0.0;
						}
					}
					try {
						Double.parseDouble(cur);
					} catch (Exception e) {
						notParseString = true;
						return 0.0;
					}
					;
					expr.add(cur);
					expr.add(signStrings[j]);
					cur = "";
					continue loop;
				}
			}
			cur += s.charAt(i);
		}
		if (!cur.equals("")) {
			try {
				Double.parseDouble(cur);
			} catch (Exception e) {
				notParseString = true;
				return 0.0;
			}
			;
			expr.add(cur);
		} else {
			notParseString = true;
			return 0.0;
		}
		loopmuldiv: while (true) {
			for (int i = 1; i < expr.size(); i += 2) {
				if (expr.get(i).equals("*")) {
					Double mul = Double.parseDouble(expr.get(i - 1))
							* Double.parseDouble(expr.get(i + 1));
					expr.set(i - 1, mul.toString());
					expr.remove(i + 1);
					expr.remove(i);
					continue loopmuldiv;

				}
				if (expr.get(i).equals("/")) {
					if (Double.parseDouble(expr.get(i + 1)) == 0) {
						nullDivString = true;
						return 0.0;
					}
					Double div = Double.parseDouble(expr.get(i - 1))
							/ Double.parseDouble(expr.get(i + 1));
					expr.set(i - 1, div.toString());
					expr.remove(i + 1);
					expr.remove(i);
					continue loopmuldiv;
				}
			}
			break;
		}
		Double ans;
		ans = Double.parseDouble(expr.get(0));

		for (int i = 1; i < expr.size(); i += 2) {
			if (expr.get(i).equals("+")) {
				ans += Double.parseDouble(expr.get(i + 1));
			}
			if (expr.get(i).equals("-")) {
				ans -= Double.parseDouble(expr.get(i + 1));
			}
		}
		return ans;
	}
}
