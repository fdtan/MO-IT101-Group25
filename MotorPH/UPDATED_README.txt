>employees.csv and attendance.csv are read using BufferedReader and FileReader

>Attendance records are loaded once into a HashMap organized by employee number, eliminating redundant file reads

>A custom parseCSVLine() method handles CSV values that contain commas inside quotes (e.g. "90,000")

>For each employee, attendance records are retrieved from the HashMap and filtered by month (June to December)

>Login and logout times are parsed and adjusted using the grace period and 5:00 PM cap rules

>Hours worked per day are stored in arrays separated by month and cutoff period
Gross salary arrays are computed by multiplying hours by the employee's hourly rate

>Deductions are computed per month using the combined 1st and 2nd cutoff gross salary via a dedicated computeDeductions() helper method:

SSS — looked up from a compensation range table
PhilHealth — 3% of monthly gross, employee pays 50% (min ₱150, max ₱900)
Pag-IBIG — 2% of monthly gross, capped at ₱100
Withholding Tax — computed from taxable income after subtracting SSS, PhilHealth, and Pag-IBIG

>Net pay for the 2nd cutoff is displayed alongside all deduction details
Program output is saved to output.txt for full payroll breakdown reference
