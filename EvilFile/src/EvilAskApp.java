import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class EvilAskApp {

	public static void main(String[] args) {

		Lists ll = new Lists();

		String minOne = "-1";
		int accountNum = 0;
		
		//create file
		File file = fileCreate();
		reading(file);
		
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Welcome to the Corp Savings and Loan");
		System.out.println("Please create the user accounts");
		
		System.out.println("Enter an account # or -1 to stop entering accounts");
		int Num = scan.nextInt();
		
		while(Num != -1){
			if(Num != -1)
				accountNum = Num;
			else
				break;
			Account newAcc = new Account();
			newAcc.setAccNum(accountNum);
			
			System.out.println("Enter name of the account# " +accountNum );
			String name = scan.next();
			newAcc.setName(name);
			
			System.out.println("enter the balance for account# " +accountNum);
			double initBal = scan.nextDouble();
			newAcc.setInitBal(initBal);
			
			newAcc.getAccNum();
			newAcc.getName();
			newAcc.getInitBal();
			
			// set the init array list with values
			ll.setInitAcc(newAcc);
			System.out.println("Enter an account # or -1 to stop entering accounts");
			Num = scan.nextInt();	
			
		}
		
		System.out.println("Enter a transaction type (Check[c], Debit card[De], Deposit or Withdrawal[D/W])"
				+ " or -1 to finish");
		String tranType = scan.next();
		while(!tranType.equals(minOne)){
			Account tranAcc = new Account();
			tranAcc.setTranType(tranType);
			
			System.out.println("Enter the account# ");
			int numIn = scan.nextInt();
			tranAcc.setAccNum(numIn);
			
			
			if (tranType.equalsIgnoreCase("c")
					|| tranType.equalsIgnoreCase("De")
					|| tranType.equalsIgnoreCase("W")) {
				System.out.println("Enter the amount of the check: ");
				double amount = -(scan.nextInt());
				tranAcc.setAmount(amount);;
				System.out
						.println("Enter the date(mm/dd/yyy) of the check: ");
				String dateIn = scan.next();
				tranAcc.setDate(processDate(dateIn));

			} else if (tranAcc.tranType.equalsIgnoreCase("D")) {
				tranAcc.amount = scan.nextInt();
				System.out.println("Enter the amount of the check: ");
				double amount = scan.nextInt();
				tranAcc.setAmount(amount);
				System.out
						.println("Enter the date(mm/dd/yyy) of the check: ");
				String dateIn = scan.next();
				tranAcc.setDate(processDate(dateIn));	
			}else{
				System.out.println("Done transaction questions");
			}
			
			tranAcc.getAccNum();
			tranAcc.getTranType();
			tranAcc.getAmount();
			
			ll.setTranAcc(tranAcc);
			System.out.println("Enter a transaction type (Check[c], Debit card[De], Deposit or Withdrawal[D/W])"
					+ " or -1 to finish");
			tranType = scan.next();
		}
		
		//calculate total tax
		calTotalBal(ll);
		//print the total
		printDetails(ll);
		
		printAllTran(ll);
		
		writeToFile(file, ll);
		
		
	}

	
	private static void writeToFile(File file, Lists ll) {
		try {
			PrintWriter write = new PrintWriter(file);
			for(Account lnewAcc : ll.getInitAcc()){
				write.println("\n");
				write.println("Account# " + lnewAcc.getAccNum());
				write.println("Account Holder's Name: " + lnewAcc.getName());
				write.println("Total Balance: " +lnewAcc.totalBal);
			}
			write.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
			
	}


	private static File fileCreate() {
		// create file process
				File file = new File("BankAccounts");
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return file;
	}
	
	
	private static void reading(File file) {
		// create file process
		BufferedReader read;
		try {
			read = new BufferedReader(new FileReader(file));
			String line = read.readLine();
			while (line != null) {
				Account fileAcc = new Account();
				fileAcc.setAccNum(Integer.parseInt(line));
				line = read.readLine();
				fileAcc.setName(line);
				fileAcc.setInitBal(Integer.parseInt(read.readLine()));
			}
			read.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}


	private static void printAllTran(Lists ll) {
		System.out.println("Printing the Transaction Summary");
		for(Account ltranAcc : ll.getTranAcc()){
			for(Account lnewAcc : ll.getInitAcc()){
				if(ltranAcc.getAccNum() == lnewAcc.getAccNum()){
					System.out.println("\n");
					System.out.println("Account# " + ltranAcc.getAccNum());
					System.out.println("Account Holder's Name: " + lnewAcc.getName());
					System.out.println("Transaction Type: " +ltranAcc.getTranType());
					System.out.println("Amount of Transaction: " + ltranAcc.getAmount());
					System.out.println("Date of Transaction: " + processReverseDate(ltranAcc.getDate()));
				}
			}
		}
		
	}


	private static String processReverseDate(long date) {
        //creating date from millisecond
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date);;
		int mYear = cal.get(cal.YEAR);
		int mMonth = cal.get(cal.MONTH);
		int mDay = cal.get(cal.DAY_OF_MONTH);
		String dd = mMonth+ "/"+ mDay + "/" +mYear;
        return dd;
	}


	private static void calTotalBal(Lists ll) {
		for(Account ltranAcc : ll.getTranAcc()){
			for(Account lnewAcc : ll.getInitAcc()){
				if(ltranAcc.getAccNum() == lnewAcc.getAccNum()){
					lnewAcc.totalBal = ltranAcc.getAmount()+lnewAcc.getInitBal(); 
					if(Math.signum(lnewAcc.totalBal)==-1.0){
						lnewAcc.setOverDraft(true);
						lnewAcc.totalBal += (-35.0);
					}
					lnewAcc.setTotalBal(lnewAcc.totalBal);
				}
			}
		}
	}

	
	private static void printDetails(Lists lst) {
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		
		for(Account listacc : lst.getInitAcc()){
			if(listacc.overDraft==true){
				System.out.println("The balance for account# " + listacc.getAccNum() + "\t" 
						+ "is " + "-" + formatter.format(listacc.getTotalBal()));
			}else{
				System.out.println("The balance for account# " + listacc.getAccNum() + "\t" 
						+ "is " + formatter.format(listacc.getTotalBal()));
			}
			
		}
	}

	public static long processDate(String tranDate) {
			long millis = 0;
			try {
				SimpleDateFormat df = new SimpleDateFormat("mm/dd/yyyy");
				df.setLenient(false);
				Date date = df.parse(tranDate);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				millis = cal.getTimeInMillis();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			return millis;
		}

	
}
