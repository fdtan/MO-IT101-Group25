import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MotorPH {

    public static void main(String[] args) throws IOException {

        String employeeFile = "employees.csv";
        String attendanceFile = "attendance.csv";

        HashMap<String, List<String[]>> attendanceMap = new HashMap<>();
        BufferedReader attLoader = new BufferedReader(new FileReader(attendanceFile));
        attLoader.readLine(); // skip header

        String attLine;
        while ((attLine = attLoader.readLine()) != null) {
            String[] attData = parseCSVLine(attLine);
            String empNum = attData[0];
            if (!attendanceMap.containsKey(empNum)) {
                attendanceMap.put(empNum, new ArrayList<>());
            }
            attendanceMap.get(empNum).add(attData);
        }
        attLoader.close();

        // Read employees
        BufferedReader empReader = new BufferedReader(new FileReader(employeeFile));
        empReader.readLine(); // skip header

        String empLine;
        while ((empLine = empReader.readLine()) != null) {
            String[] empData = parseCSVLine(empLine);
            String empNumber = empData[0];
            String lastName = empData[1];
            String firstName = empData[2];
            String birthday = empData[3];
            double hourlyRate = Double.parseDouble(empData[18]);

            double[] hoursFirst = new double[7];
            double[] hoursSecond = new double[7];

            // look up attendance from HashMap
            List<String[]> records = attendanceMap.getOrDefault(empNumber, new ArrayList<>());
            for (String[] attData : records) {
                String date = attData[3];
                String loginTime = attData[4];
                String logoutTime = attData[5];

                String[] dateParts = date.split("/");
                int day = Integer.parseInt(dateParts[1]);
                int month = Integer.parseInt(dateParts[0]);

                String[] loginParts = loginTime.split(":");
                String[] logoutParts = logoutTime.split(":");
                int loginHour = Integer.parseInt(loginParts[0]);
                int loginMin = Integer.parseInt(loginParts[1]);
                int logoutHour = Integer.parseInt(logoutParts[0]);
                int logoutMin = Integer.parseInt(logoutParts[1]);

                if (loginHour == 8 && loginMin <= 10) {
                    loginHour = 8;
                    loginMin = 0;
                }

                if (logoutHour > 17 || (logoutHour == 17 && logoutMin > 0)) {
                    logoutHour = 17;
                    logoutMin = 0;
                }

                double hoursWorked = (logoutHour - loginHour) + (logoutMin - loginMin) / 60.0;
                if (hoursWorked < 0) hoursWorked = 0;

                for (int i = 6; i <= 12; i++) {
                    if (month == i) {
                        int index = i - 6;
                        if (day >= 1 && day <= 15) {
                            hoursFirst[index] += hoursWorked;
                        } else {
                            hoursSecond[index] += hoursWorked;
                        }
                    }
                }
            }

            double[] grossFirst = new double[7];
            double[] grossSecond = new double[7];
            for (int i = 0; i < 7; i++) {
                grossFirst[i] = hourlyRate * hoursFirst[i];
                grossSecond[i] = hourlyRate * hoursSecond[i];
            }

            String[] monthNames = {"June", "July", "August", "September", "October", "November", "December"};

            System.out.println("Employee #: " + empNumber + " | Name: " + firstName + " " + lastName + " | Birthday: " + birthday + " | Hourly Rate: " + hourlyRate);

            for (int i = 0; i < 7; i++) {
                double monthlyGross = grossFirst[i] + grossSecond[i];

                // call helper method for deductions
                double[] deductions = computeDeductions(monthlyGross);
                double sss = deductions[0];
                double philHealth = deductions[1];
                double pagIbig = deductions[2];
                double withholdingTax = deductions[3];
                double totalDeductions = deductions[4];

                double netSecondCutoff = grossSecond[i] - totalDeductions;

                System.out.println("  " + monthNames[i] + ":");
                System.out.println("    1st Cutoff Gross: " + grossFirst[i]);
                System.out.println("    2nd Cutoff Gross: " + grossSecond[i]);
                System.out.println("    Monthly Gross: " + monthlyGross);
                System.out.println("    SSS: " + sss);
                System.out.println("    PhilHealth: " + philHealth);
                System.out.println("    Pag-IBIG: " + pagIbig);
                System.out.println("    Withholding Tax: " + withholdingTax);
                System.out.println("    Total Deductions: " + totalDeductions);
                System.out.println("    2nd Cutoff Net Pay: " + netSecondCutoff);
            }
            System.out.println();
        }
        empReader.close();
    }

    // helper method
    private static double[] computeDeductions(double monthlyGross) {
        // PhilHealth
        double philHealth = (monthlyGross * 0.03) / 2;
        if (philHealth < 150) philHealth = 150;
        if (philHealth > 900) philHealth = 900;

        // Pag-IBIG
        double pagIbig = monthlyGross * 0.02;
        if (pagIbig > 100) pagIbig = 100;

        // SSS
        double sss = 0;
        if (monthlyGross < 3250) sss = 135;
        else if (monthlyGross < 3750) sss = 157.5;
        else if (monthlyGross < 4250) sss = 180;
        else if (monthlyGross < 4750) sss = 202.5;
        else if (monthlyGross < 5250) sss = 225;
        else if (monthlyGross < 5750) sss = 247.5;
        else if (monthlyGross < 6250) sss = 270;
        else if (monthlyGross < 6750) sss = 292.5;
        else if (monthlyGross < 7250) sss = 315;
        else if (monthlyGross < 7750) sss = 337.5;
        else if (monthlyGross < 8250) sss = 360;
        else if (monthlyGross < 8750) sss = 382.5;
        else if (monthlyGross < 9250) sss = 405;
        else if (monthlyGross < 9750) sss = 427.5;
        else if (monthlyGross < 10250) sss = 450;
        else if (monthlyGross < 10750) sss = 472.5;
        else if (monthlyGross < 11250) sss = 495;
        else if (monthlyGross < 11750) sss = 517.5;
        else if (monthlyGross < 12250) sss = 540;
        else if (monthlyGross < 12750) sss = 562.5;
        else if (monthlyGross < 13250) sss = 585;
        else if (monthlyGross < 13750) sss = 607.5;
        else if (monthlyGross < 14250) sss = 630;
        else if (monthlyGross < 14750) sss = 652.5;
        else if (monthlyGross < 15250) sss = 675;
        else if (monthlyGross < 15750) sss = 697.5;
        else if (monthlyGross < 16250) sss = 720;
        else if (monthlyGross < 16750) sss = 742.5;
        else if (monthlyGross < 17250) sss = 765;
        else if (monthlyGross < 17750) sss = 787.5;
        else if (monthlyGross < 18250) sss = 810;
        else if (monthlyGross < 18750) sss = 832.5;
        else if (monthlyGross < 19250) sss = 855;
        else if (monthlyGross < 19750) sss = 877.5;
        else if (monthlyGross < 20250) sss = 900;
        else if (monthlyGross < 20750) sss = 922.5;
        else if (monthlyGross < 21250) sss = 945;
        else if (monthlyGross < 21750) sss = 967.5;
        else if (monthlyGross < 22250) sss = 990;
        else if (monthlyGross < 22750) sss = 1012.5;
        else if (monthlyGross < 23250) sss = 1035;
        else if (monthlyGross < 23750) sss = 1057.5;
        else if (monthlyGross < 24250) sss = 1080;
        else if (monthlyGross < 24750) sss = 1102.5;
        else sss = 1125;

        // Withholding Tax
        double taxableIncome = monthlyGross - sss - philHealth - pagIbig;
        double withholdingTax = 0;
        if (taxableIncome <= 20832) {
            withholdingTax = 0;
        } else if (taxableIncome <= 33332) {
            withholdingTax = (taxableIncome - 20833) * 0.20;
        } else if (taxableIncome <= 66666) {
            withholdingTax = 2500 + (taxableIncome - 33333) * 0.25;
        } else if (taxableIncome <= 166666) {
            withholdingTax = 10833 + (taxableIncome - 66667) * 0.30;
        } else if (taxableIncome <= 666666) {
            withholdingTax = 40833.33 + (taxableIncome - 166667) * 0.32;
        } else {
            withholdingTax = 200833.33 + (taxableIncome - 666667) * 0.35;
        }

        double totalDeductions = sss + philHealth + pagIbig + withholdingTax;
        return new double[]{sss, philHealth, pagIbig, withholdingTax, totalDeductions};
    }

    static String[] parseCSVLine(String line) {
        java.util.List<String> result = new java.util.ArrayList<>();
        boolean inQuotes = false;
        StringBuilder current = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString().trim());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        result.add(current.toString().trim());
        return result.toArray(new String[0]);
    }
}
