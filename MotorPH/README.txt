TEAM DETAILS:
Group 25
Francesca Diane Tan
Francesca Angelica Catayas
-----------------------------------------------------------------------

PROGRAM DETAILS:

>employees.csv and attendance.csv are read using BufferedReader and FileReader

>a custom parseCSVLine() method handles CSV values that contain commas inside quotes (example: "90,000")

>for each employee, attendance records are matched by employee number and filtered by month (june to dec)

>login and logout times are parsed and adjusted using the grace period and 5:00 PM cap rules

>hours worked per day are stored in arrays separated by month and cutoff period

>gross salary arrays are computed by multiplying hours by the employee's hourly rate

>deductions are computed per month using the combined 1st and 2nd cutoff gross salary:
-SSS — looked up from a compensation range table
-PhilHealth — 3% of monthly gross, employee pays 50% (min ₱150, max ₱900)
-Pag-IBIG — 2% of monthly gross, capped at ₱100
-Withholding Tax — computed from taxable income after subtracting SSS, PhilHealth, and Pag-IBIG

>net pay for the 2nd cutoff is displayed alongside all deduction details
---------------------------------------------------------------------------

PROJECT PLAN: https://docs.google.com/spreadsheets/d/1eC1D3OBSizcpTTLOCFsb8p_LYWgPdX0fczCAZk2ayMk/edit?usp=sharing
