---
  layout: default.md
  title: "User Guide"
  pageNav: 3
---

# CLInic User Guide

Welcome to **CLInic**, your digital assistant for managing patients and appointments! CLInic is a desktop app designed for clinic assistants, optimized for use via a Command Line Interface (CLI) while still offering the benefits of a Graphical User Interface (GUI). <br/> <br/>If you're familiar with digitalized software or have used a CLI before, you'll find CLInic intuitive. Don't worry if you're new to CLI; we'll guide you through every step.

As part of our Beta Testing, we would greatly appreciate feedback from actual users. Help us improve CLInic together [**here**](https://forms.gle/RSBeinMHPMYXyYyGA)!

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------
## Introducing CLInic

CLInic is designed to keep track of your patient data and appointment schedules. To ensure a smooth and focused user experience, we have specified certain requirements for patient and appointment data.

**Patient**
* Each patient is identified by a unique `NRIC`
* A patient has: NRIC, Name, Date of Birth, Phone Number, Email, Address, Tags
* A patient can be: added, deleted, edited, found

Restrictions:
* A patient's `NRIC` is restricted to Singapore's official NRIC format. 
  * CLInic assumes that foreign patients may instead use a Foreign Identification Number (FIN) that is according to the Singaporean NRIC format.

**Appointment**
* An appointment belongs to one patient. 
* Each appointment is identified by a unique `NRIC`, `DATE` and `START_TIME`
* An appointment has: NRIC, Date, Start Time, End Time, Appointment Type, Note
* An appointment can be: added, deleted, edited, found, marked, unmarked

Restrictions:
* An appointment **cannot** be added if it overlaps with an existing appointment for the same patient. Otherwise, it will be flagged as seen [here](#adding-an-appointment-addappt-or-aa).
  * CLInic allows appointments of different patients to overlap as they may be seen concurrently by different doctors or have different tests.
* An appointment **cannot** span across different days or be overnight.
  * CLInic allows appointments to be made anytime within a single day **but not overnight** to simplify daily operations and avoid ambiguity.


--------------------------------------------------------------------------------------------------------------------

## Quick start

1. System Requirements: Ensure you have [**Java 11**](https://www.oracle.com/java/technologies/downloads/#java11) or above installed on your computer.

1. Download the latest `CLInic.jar` from [**here**](https://github.com/AY2324S2-CS2103T-F10-3/tp/releases).

1. Save the file to a location on your computer that will serve as your home folder for CLInic.

1. Open a command terminal on your computer. If you're unsure how to do this, we'll walk you through it.
   * **Windows**: Press `Win + R`, type `cmd`, and press `Enter`.
   * **MacOS**: Press `Cmd + Space`, type `Terminal`, and press `Enter`.
   * **Linux**: Press `Ctrl + Alt + T`.

1. Navigate to the folder where you saved the `CLInic.jar` file. If you saved it in your `Downloads` folder, you can use the following commands:
   * **Windows**: `cd Downloads`
   * **MacOS**: `cd ~/Downloads`
   * **Linux**: `cd ~/Downloads`

1. Type `java -jar CLInic.jar` command into terminal to run the application.<br>
      A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
      ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all patients and appointments.

   * `addPatient i/T0123456A n/John Doe b/2001-05-02 p/98765432 e/johnd@example.com a/John street, block 123, #01-01` : Adds a contact named `John Doe` to CLInic.

   * `deletePatient i/T0123456A` : Deletes patient with NRIC T0123456A and corresponding appointments.

   * `clear` : Deletes all patients and appointments.

   * `exit` : Exits the app.

1. Refer to the [**Features**](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<box type="info" seamless>

**Notes about the command format:**<br>

* Commands are case-sensitive, including shorthand formats.<br>
  e.g Invalid commands like `AddPatient`, `addpatient`, `Addpatient`, `AP`, `aP` and `Ap` will not be recognised by CLInic. 

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `addPatient n/NAME`, `NAME` is a parameter which can be used as `addPatient n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit`, `switchView` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
  
**Notes about the data:**<br>

* Initiating CLInic without CLInic.json will result in the loading of dummy data into CLInic.
</box>

### 1. Viewing help : `help`

If you are facing any issues while using CLInic, you can use this help command which will provide you with a link to this User Guide!

**Format:**
<box>

`help`
</box>

![help message](images/helpMessage.png)

### 2. Patient Commands

**Input Fields:**

 Prefix | Field                                                                            | Constraints                                                                                                                                                                                                                                                       |
|----------------|----------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **i/**         | Unique ID in Singapore's context - NRIC (e.g. `T0123456A`)                       | - Possible invalid NRICs not accounted for due to uncertainty in checksum of Singapore's system and FIN numbers. <br/> - Also allowing for NRICs beyond current date e.g. `T99...` to allow flexibility of app without having to constantly readjust fields <br/> | 
| **n/**         | Name of patient.                                                                 | - Restricted to alphanumeric characters. <br/> - Extra spacing is allowed within the name to allow for user convenience and flexibility.                                                                                                                          |
| **b/**         | Date of birth of patient.                                                        | - Dates must be in YYYY-MM-DD format.<br/>- Only allows valid dates after 1990-01-01.                                                                                                                                                                             |
| **p/**         | Emergency contact number.                                                        | - Only Singapore phone numbers allowed. <br/> - Duplicate phone numbers allowed in case of children with parent's contact number.                                                                                                                                 |
| **e/**         | Email of patient.                                                                | NA                                                                                                                                                                                                                                                                |
| **a/**         | Address of patient.                                                              | NA                                                                                                                                                                                                                                                                |
| **t/**         | Tag attached to specify patient's medical allergies. e.g. `Paracetamol, Insulin` | - No constraints to allow for flexiblility, although it is recommended to use this tag for medical allergies.                                                                                                                                                     

<box type="wrong" light>

**Possible invalid input fields.**
<box type="tip" seamless>

Some of the inputs you have keyed in are invalid, check out the constraints for the input fields above to understand what values CLInic accepts.
</box>
</box>

### <a name="addPatient"></a>2.1 Adding a patient: `addPatient` OR `ap`

Use this command if you wish to add a new patient to CLInic. You would be required to specify important patient personal information and can include the patient's medical allergies, if any.

**Format:**
<box>

Full: `addPatient i/NRIC n/NAME b/DOB p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/MEDICAL_ALLERGY]…​ `

Shorthand: `ap i/NRIC n/NAME  b/DOB p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/MEDiCAL_ALLERGY]…​`
</box>

<box type="warning" seamless>

A patient must have a unique NRIC in CLInic.

</box>

**Examples:**
<box>

`addPatient i/T0123456A n/John Doe b/2001-05-02 p/98765432 e/johnd@example.com a/John street, block 123, #01-01`
</box>
<box>

`addPatient i/S9876543A n/Betsy Crowe b/1998-02-03 t/Insulin e/betsycrowe@example.com a/Crowe street, block 234, #12-12 p/91234567 t/Paracetemol`
</box>

<box>

`ap i/T0123456A n/John Doe b/2001-05-02 p/98765432 e/johnd@example.com a/John street, block 123, #01-01`
</box>

<box type="success" light>

**Expected Outcome**:
![Add patient expected outcome](./images/addPatient.png)

</box>

<box type="wrong" light>

**This patient already exists in CLInic.**

<box type="tip" seamless>

There already exists a patient with the NRIC you specified. To view the details of that patient, you can use the [findPatient](#findPatient) command.
</box>
</box>

### 2.2 Deleting a patient : `deletePatient` OR `dp`

Use this command if you wish to delete a patient from CLInic.
Corresponding appointments for the specified patient will be deleted too.

<box type="warning" seamless>

Corresponding appointments for the specified patient will be deleted from CLInic too.

</box>

**Format:**
<box>

Full: `deletePatient i/NRIC`

Shorthand: `dp i/NRIC`
</box>

**Examples:**
<box>

`deletePatient i/S9876543A`
</box>

<box type="wrong" light>

**The NRIC provided is not found in the system.**

<box type="tip" seamless>

CLInic does not have a patient with the provided NRIC, please double-check the NRIC provided.
</box>
</box>

### 2.3 Editing a patient : `editPatient` OR `ep`

Use this command if you wish to edits an existing patient in CLInic.

**Format:**
<box>

Format: `editPatient i/NRIC [newn/NEW_NAME] [newb/NEW_DOB] [newp/NEW_PHONE] [newe/NEW_EMAIL] [newa/NEW_ADDRESS] [newt/NEW_MEDICAL_ALLERGY]…​`

Shorthand: `ep i/NRIC [newn/NEW_NAME] [newb/NEW_DOB] [newp/NEW_PHONE] [newe/NEW_EMAIL] [newa/NEW_ADDRESS] [newt/NEW_MEDICAL_ALLERGY]…​`
</box>

<box type="warning" seamless>

Existing values will be updated to the input values.

When editing tags, existing tags of the patient will be removed, i.e., adding tags is not cumulative. Use t/ to remove all tags.

</box>

**Examples:**
<box>

> Edits the phone number and email address of the patient with NRIC:`T0123456A` to be `91234567` and `johndoe@example.com` respectively.

`editPatient i/T0123456A newp/91234567 newe/johndoe@example.com`
</box>

<box>

> Edits the name of the patient with NRIC:`S8765432Z` to be `Betsy Crower` and clears all existing tags.

`editPatient i/S98765432A newn/Betsy Crower newt/`
</box>

<box>

> Executes the above command but uses shorthand format

`ep i/S98765432A newn/Betsy Crower newt/`
</box>

<box type="wrong" light>

**At least one field to edit must be provided.**

<box type="tip" seamless>

CLInic requires that at least one optional field is provided to execute the `editPatient` command.
</box>
</box>

<box type="wrong" light>

**The NRIC provided is not found in the system.**

<box type="tip" seamless>

CLInic does not have a patient with the provided NRIC, please double-check the NRIC provided or create a Patient using the [addPatient](#addPatient) command.
</box>
</box>

### <a name="findPatient"></a>2.4 Finding patients: `findPatient` OR `fp`

Use this command if you wish to finds patients whose name OR NRIC fit the given keywords.

**Format:**
<box>

Format: `findPatient n/NAME_KEYWORD [MORE_NAME_KEYWORDS]` OR `findPatient i/NRIC_KEYWORD`

Shorthand: `fp n/NAME_KEYWORD [MORE_NAME_KEYWORDS]` OR `fp i/NRIC_KEYWORD`
</box>

<box type="warning" seamless>

The search is case-insensitive. e.g `hans` will match `Hans`.

Partial words will be matched only if the start of the word is the same e.g. `T01` will match `T0123456A`.

To accommodate for future extensions, special characters can be searched. However, no search results may be found as special characters are currently not supported in `NAME` and `NRIC`.

If currently on Day View, this command will cause a `switchView` to automatically occur.
</box>

<box type="wrong" light>

**Find by either NRIC or name, not both!**

<box type="tip" seamless>

CLInic currently only supports finding patients by a single field. 
</box>
</box>

#### 2.4.1 Name Search

**Examples:**
<box>

> Find all patients with name beginning with `john`

`findPatient n/John`
</box>

**Examples:**
<box>

> Find all patients with name beginning with either `alex` or `david`, using shorthand command

`fp n/ alex david`
</box>

#### 2.4.2 NRIC Search
**Examples:**
<box>

> Find all patients with NRIC born in the year 2001, with NRIC starting with `t01`

`findPatient i/t01`
</box>

<box type="wrong" light>

**You have provided more than one word of NRIC keywords to match.**

<box type="tip" seamless>

CLInic does not provide support for finding patients with different starting NRICs. Please only provide one starting NRIC.

e.g. `n/T01 T012` will NOT return `T0123456A` as the given keyword is `T01 T012`
</box>
</box>

  ![result for 'find alex david'](images/findAlexDavidResult.png)

### Adding an Appointment: `addAppt` OR `aa`

Adds an appointment to the CLInic.

Format: `addAppt i/NRIC d/DATE from/START_TIME to/END_TIME t/APPOINTMENT_TYPE [note/NOTE]` <br/>
Shorthand: `aa i/NRIC d/DATE from/START_TIME to/END_TIME t/APPOINTMENT_TYPE [note/NOTE]`

* Adds an appointment for the patient with specified `NRIC`, on `DATE` from `START_TIME` to `END_TIME`
* Patient with this NRIC **must exist within database**.
* Details of `APPOINTMENT_TYPE` and `NOTE` will be captured for reference
* You cannot schedule an appointment for a patient on a date before their date of birth
* `note/` is an optional field

<box type="tip" seamless>

**Tip:** If new appointment overlaps with an existing appointment for the same patient, all overlapping appointments will be shown on Overall View. If currently on Day View, see [here](#switch-between-overall-view-and-day-view--switchview-or-sv).

</box>

Examples:
* `addAppt i/T0123456A d/2024-02-20 from/11:00 to/11:30 t/Medical Check-up note/Routine check-in`
* `addAppt i/S1234567A d/2024-02-20 from/15:00 to/15:30 t/Blood Test note/Follow-up from last consultation`
* `aa i/S1234567A d/2024-02-20 from/15:00 to/15:30 t/Blood Test note/Follow-up from last consultation`

### Deleting an Appointment: `deleteAppt` OR `da`

Deleting an appointment from CLInic.

Format: `deleteAppt i/NRIC d/DATE from/START_TIME` <br/>
Shorthand: `da i/NRIC d/DATE from/START_TIME`

* Deletes an appointment for the patient with specified `NRIC`, on `DATE` from `START_TIME`.
* Appointment with the stated details **must exist within database**.
* `END_TIME` not needed as same patient can never have overlapping appointments, hence `START_TIME` is unique

Examples:
* `deleteAppt i/S8743880A d/2024-02-20 from/11:00`
* `da i/S8743880A d/2024-02-20 from/11:00`

### Editing an Appointment : `editAppointment` OR `ea`

Edits an existing appointment in CLInic.

Format: `editAppt i/NRIC d/DATE from/START_TIME [newd/NEW_DATE] [newfrom/NEW_START_TIME] [newto/NEW_END_TIME] [newt/NEW_APPOINTMENT_TYPE] [newnote/NEW_NOTE]` <br/>
Shorthand: `ea i/NRIC d/DATE from/START_TIME [newd/NEW_DATE] [newfrom/NEW_START_TIME] [newto/NEW_END_TIME] [newt/NEW_APPOINTMENT_TYPE] [newnote/NEW_NOTE]`

* Edits the appointment with the specified NRIC, DATE and START_TIME.
* Ensure the NRIC is valid and exists in the system.
* Provide at least one optional field for editing.
* Existing values will be updated to the input values.

<box type="tip" seamless>

**Tip:** If edited appointment overlaps with an existing appointment for the same patient, all overlapping appointments will be shown on Overall View. If currently on Day View, see [here](#switch-between-overall-view-and-day-view--switchview-or-sv).

</box>

Examples:
*  `editAppt i/T0123456A d/2024-02-20 from/11:00 newd/2024-02-21`
  * Edits the date of the appointment with NRIC:`T0123456A`, DATE: `2024-02-20`, START_TIME: `11:00`, to be `2024-02-21` instead.
*  `editAppt i/S8743880A d/2024-10-20 from/14:00 newnote/ `
  * Clears note for appointment with NRIC:`S8743880A`, DATE: `2024-10-20`, START_TIME: `14:00`.
*  `ea i/S8743880A d/2024-10-20 from/14:00 newnote/ `

### Finding appointments: `findAppt` OR `fa`

Finds appointments based on the given parameters.

Format: `findAppt [i/NRIC] [d/DATE] [from/START_TIME]`
Shorthand: `fa [i/NRIC] [d/DATE] [from/START_TIME]`

* Filters an appointment with specific `NRIC`, `DATE` or `START_TIME` (any combination of the 3)
* If invalid parameters, error detailing what went wrong will be displayed.
* For argument concerning TIME, all appointments that start at the given time and later than that are returned.
* Fetching for TIME without DATE will return all appointments whose start from that time or later than that on any date.

<box type="tip" seamless>

**Tip:** If currently on Day View, this command will cause a `switchView` to automatically occur. 

</box>

Examples:
* `findAppt d/ 2024-02-20 from/ 11:00`
* `fa d/ 2024-02-20 from/ 11:00`
*  returns you all appointments on `2024-02-20` starting from `11:00` and later.

### Marking an Appointment: `mark`

Marks an appointment from the address book.

Format: `mark i/NRIC d/DATE from/START_TIME`

* Marks an appointment for the patient with specified `NRIC`, on `DATE` from `START_TIME`
* Appointment with the stated details **must exist within database**.
* `END_TIME` not needed as same patient can never have overlapping appointments, hence `START_TIME` is unique

Examples:
* `mark i/T0123456A d/2024-02-20 from/11:00`

### Unmarking an Appointment: `unmark`

Unmarks an appointment from the address book.

Format: `unmark i/NRIC d/DATE from/START_TIME`

* Unmarks an appointment for the patient with specified `NRIC`, on `DATE` from `START_TIME`
* Appointment with the stated details **must exist within database**.
* `END_TIME` not needed as same patient can never have overlapping appointments, hence `START_TIME` is unique

Examples:
* `unmark i/T0123456A d/2024-02-20 from/11:00`

### Listing all patients and appointments : `list` OR `ls`

Shows a list of all patients and appointments in CLInic.

Format: `list` OR `ls`

### Switch between Overall View and Day View : `switchView` OR `sv`

Switches view from Overall View to Day View or vice versa.

Format: `switchView` OR `sv`

### Clearing all entries : `clear`

Clears all entries of patients and appointments from CLInic.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

CLInic data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

CLInic data are saved automatically as a JSON file `[JAR file location]/data/CLInic.json`. Advanced users are welcome to update data directly by editing that data file.

<box type="warning" seamless>

**Caution:**
If your changes to the data file makes its format invalid, CLInic will discard all data and start with an empty data file at the next run.  Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the CLInic to behave in unexpected ways (e.g., if a value entered is outside the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</box>

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous CLInic home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **When missing read/write permissions**, the application may not work. Ensure that read/write permissions are enabled for CLInic.

--------------------------------------------------------------------------------------------------------------------

## Glossary
| Term                 | Explanation                                                                                                                                    |
|----------------------|------------------------------------------------------------------------------------------------------------------------------------------------|
| **CLI**              | Command Line Interface, a text-based interface for interacting with software by typing commands.                                               |
| **GUI**              | Graphical User Interface, a visual interface that allows users to interact with software using graphical elements such as windows and buttons. |
| **JSON**             | JavaScript Object Notation, a lightweight data-interchange format.                                                                             |
| **NRIC**             | National Registration Identity Card, a unique identifier for individuals in Singapore.                                                         |
| **TAG**              | A keyword or term assigned to a piece of information, making it easier to search for and organize.                                             |
| **APPOINTMENT_TYPE** | The type of appointment, e.g., Medical Check-up, Blood Test, etc.                                                                              |
| **NOTE**             | Additional information or comments about an appointment.                                                                                       |

--------------------------------------------------------------------------------------------------------------------

## Prefix summary for patients
| Prefix | Field                                                                             | Caveats                                                                                                                                                                                                                                                                                                                                                                       
|-----------------|-----------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **i/**          | Unique ID in Singapore's context - NRIC (e.g. `T0123456A`)                        | - Possible invalid NRICs not accounted for due to uncertainty in checksum of Singapore's system and FIN numbers. <br/> - Also allowing for NRICs beyond current date e.g. `T99...` to allow flexibility of app without having to constantly readjust fields <br/> - For foreign visitors, placeholder NRIC eg. `K0000001A`, since foreigners should not be staying long-term. |
| **n/**          | Name of patient.                                                                  | - Will restrict to alphanumeric characters to avoid parsing errors, since data is stored as JSON. <br/> - Extra spacing is allowed within the name to allow for user convenience and flexibility.                                                                                                                                                                             |                                                                 |
| **b/**          | Date of birth of patient.                                                         | - Only allows valid dates after 1 Jan 1990.                                                                                                                                                                                                                                                                                                                                   |
| **p/**          | Emergency contact number.                                                         | - Only Singapore phone numbers allowed. <br/> - Duplicate phone numbers allowed in case of children with parent's contact number.                                                                                                                                                                                                                                             |
| **e/**          | Email of patient.                                                                 |                                                                                                                                                                                                                                                                                                                                                                               |
| **a/**          | Address of patient.                                                               |                                                                                                                                                                                                                                                                                                                                                                               |
| **t/**          | Tag attached to patient for extra information. e.g. `Fall risk, Hokkien speaking` |                                                                                                                                                                                                                                                                                                                                                                               |
| **newn/**       | New name of patient if change required.                                           |                                                                                                                                                                                                                                                                                                                                                                               |
| **newp/**       | New phone number of patient if change required.                                   |                                                                                                                                                                                                                                                                                                                                                                               |
| **newe/**       | New email of patient if change required.                                          |                                                                                                                                                                                                                                                                                                                                                                               |
| **newa/**       | New address of patient if change required.                                        |                                                                                                                                                                                                                                                                                                               
| **newt/**       | New tag of patient if change required.                                            |

## Prefix summary for appointments
| Prefix | Field                                                                                      | Caveats                                                                                                                                                                                                                                                           
|----------------------|--------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **i/**               | Unique ID in Singapore's context - NRIC <br/> (e.g. `T0123456A`) <br/> to identify patient | - Possible invalid NRICs not accounted for due to uncertainty in checksum of Singapore's system and FIN numbers. <br/> - Also allowing for NRICs beyond current date e.g. `T99...` to allow flexibility of app without having to constantly readjust fields <br/> - For foreign visitors, placeholder NRIC eg. `K0000001A`, since foreigners should not be staying long-term. |
| **d/**               | Date of appointment in YYYY-MM-DD format e.g. `2024-02-20`                                 | - Valid dates after 1 Jan 1990                                                                                                                                                                                                                                    |                                                                                                                                                                                                                                                         |
| **from/**            | Start time of appointment in HH:mm format e.g. `13:00`                                     | - Start time has to be earlier than end time                                                                                                                                                                                                                      |
| **to/**              | End time of appointment in HH:mm format e.g. `14:30`                                       | - End time has to be later than start time <br/> - To timing is taken to be on same day as `from/`                                                                                                                                                                |
| **t/**               | Appointment type e.g. `Medical check-up`                                                   |
| **note/**            | Additional notes for appointment e.g. `X-ray`                                              |
| **newd/**            | New date of appointment if change required.                                                |
| **newfrom/**         | New start time of appointment if change required.                                          
| **newto/**           | New end time of appointment if change required.                                            
| **newt/**            | New type of appointment if change required.                                                |
| **newnote/**         | New note of appointment if change required.                                                |

### Additional Information
- Our commands check for the validity of the input data and will prompt the user if the input is invalid.
- Usage of prefixes (from this list above) in commands that do not require them will result in an error.
- This includes unintentional use of known prefixes in other fields
--------------------------------------------------------------------------------------------------------------------

## Command summary
| Action            | Format, Examples                                                                                                                                                                                                 |
|-------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **AddPatient**    | `addPatient i/NRIC n/NAME b/DOB p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​` <br> e.g., `addPatient i/T0123456A n/John Doe b/2001-05-02 p/98765432 e/johnd@example.com a/John street, block 123, #01-01`          |
| **DeletePatient** | `deletePatient i/NRIC`<br> e.g., `deletePatient i/T0123456A`                                                                                                                                                     |                                                                 |
| **EditPatient**   | `editPatient i/NRIC [newn/NEW_NAME] [newp/NEW_PHONE_NUMBER] [newe/NEW_EMAIL] [newa/NEW_ADDRESS] [newt/NEW_TAG]…​`<br> e.g.,`editPatient i/T0123456A newn/James Lee newe/jameslee@example.com`                    |
| **FindPatient**   | `findPatient n/NAME_KEYWORD [MORE_NAME_KEYWORDS]` OR `findPatient i/NRIC_KEYWORD`<br> e.g., `findPatient n/James Jake`                                                                                                        |
| **AddAppt**       | `addAppt i/NRIC d/DATE from/START_TIME to/END_TIME t/APPOINTMENT_TYPE note/NOTE`<br> e.g., `addAppt i/T0123456A d/2024-02-20 from/11:00 to/11:30 t/Medical Check-up note/Routine check-in`                       |
| **DeleteAppt**    | `deleteAppt i/NRIC d/DATE from/START_TIME` <br> e.g., `deleteAppt i/S8743880A d/2024-02-20 from/11:00`                                                                                                           |
| **EditAppt**      | `editAppt i/NRIC d/DATE from/START_TIME [newd/NEW_DATE] [newfrom/NEW_START_TIME] [newto/NEW_END_TIME] [newt/NEW_APPOINTMENT_TYPE] [newnote/NEW_NOTE]` <br> e.g., `editAppt i/T0123456A d/2024-02-20 from/11:00 newd/2024-02-21` |
| **FindAppt**      | `findAppt [i/NRIC] [d/DATE] [from/START_TIME]` <br> e.g., `findAppt i/T0123456A d/2024-02-20 from/11:00`                                                                                                         |
| **Mark**          | `mark i/NRIC d/DATE from/START_TIME` <br> e.g., `mark i/T0123456A d/2024-02-20 from/11:00`                                                                                                                       |
| **Unmark**        | `unmark i/NRIC d/DATE from/START_TIME` <br> e.g., `unmark i/T0123456A d/2024-02-20 from/11:00`                                                                                                                   |
| **List**          | `list`                                                                                                                                                                                                           
| **SwitchView**    | `switchView`                                                                                                                                                                                                     
| **Clear**         | `clear`                                                                                                                                                                                                          |
| **Exit**          | `exit`                                                                                                                                                                                                           |
| **Help**          | `help`                                                                                                                                                                                                           |
