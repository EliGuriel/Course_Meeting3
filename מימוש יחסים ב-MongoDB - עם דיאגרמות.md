
<div dir="rtl">

# מימוש יחסים ב-MongoDB - עם דיאגרמות

## מבוא

MongoDB, כבסיס נתונים לא-רלציוני (NoSQL - Not Only SQL, מערכת שלא מבוססת על המודל הרלציוני המסורתי עם טבלאות קשיחות), מציע גישות שונות למימוש יחסים בין אובייקטים. בניגוד לבסיסי נתונים רלציוניים המסורתיים שמשתמשים בטבלאות ומפתחות זרים (Foreign Keys - עמודות המכילות מזהים שמקשרים בין טבלאות שונות), MongoDB מאפשר גמישות רבה יותר בדרך שבה אנו מייצגים ומממשים קשרים בין ישויות.

מסמך זה מציג את האסטרטגיות השונות למימוש יחסים ב-MongoDB, עם דגש מיוחד על יחסי רבים-לרבים, תוך שימוש בדיאגרמות Mermaid (כלי ליצירת דיאגרמות טקסטואליות) להמחשה.

## סוגי יחסים במסדי נתונים

### יחס אחד-לאחד (One-to-One)

יחס שבו לכל רשומה בקולקציה (Collection - מקבילה לטבלה בבסיסי נתונים רלציוניים, אך גמישה יותר) אחת יש קשר עם רשומה אחת בדיוק בקולקציה אחרת. דוגמה קלאסית: אדם ודרכון - לכל אדם יש דרכון אחד, ולכל דרכון יש בעלים אחד.

![ER Diagram](./diagrams/er-diagram-has.png)

</div>

<div dir="ltr">

```mermaid
erDiagram
    PERSON ||--|| PASSPORT : has
    PERSON {
        string _id
        string name
        int age
    }
    PASSPORT {
        string _id
        string personId
        string number
        date expiryDate
    }
```
</div>

<div dir="rtl">

### יחס אחד-לרבים (One-to-Many)

יחס שבו לרשומה אחת בקולקציה יש קשר עם מספר רשומות בקולקציה אחרת, אך כל רשומה מהצד ה"רבים" שייכת לרשומה אחת בלבד. דוגמה: סופר וספרים - לסופר יכולים להיות ספרים רבים, אך כל ספר יש לו סופר אחד.

![ER Diagram](./diagrams/er-diagram-one-many.png)

</div>

<div dir="ltr">

```mermaid
erDiagram
    AUTHOR ||--o{ BOOK : writes
    AUTHOR {
        string _id
        string name
        string biography
    }
    BOOK {
        string _id
        string authorId
        string title
        int year
    }
```

</div>

<div dir="rtl">

### יחס רבים-לרבים (Many-to-Many)

יחס שבו לרשומות בקולקציה אחת יש קשרים מרובים עם רשומות בקולקציה אחרת, ולהיפך. זהו היחס המורכב ביותר. דוגמה: סטודנטים וקורסים - כל סטודנט יכול ללמוד מספר קורסים, וכל קורס יכול להכיל מספר סטודנטים.

![ER Diagram](./diagrams/er-diagram-many-many.png)

</div>

<div dir="ltr">

```mermaid
erDiagram
    STUDENT }|--o{ ENROLLMENT : has
    COURSE }|--o{ ENROLLMENT : has
    STUDENT {
        string _id
        string name
        string email
    }
    COURSE {
        string _id
        string name
        string department
    }
    ENROLLMENT {
        string _id
        string studentId
        string courseId
        string grade
    }
```

</div>

<div dir="rtl">

## אסטרטגיות מימוש יחסים ב-MongoDB

### 1. הטמעה (Embedding)

הטמעת מסמכים שלמים זה בתוך זה. זוהי גישה ייחודית לבסיסי נתונים מסמכיים (Document Databases), שבה המידע הקשור נשמר פיזית בתוך אותו מסמך JSON/BSON (Binary JSON - פורמט בינארי יעיל של MongoDB). גישה זו מתאימה כאשר הנתונים נקראים תמיד יחד וגודל המסמך לא גדל ללא גבול.

#### יתרונות הטמעה:
- **ביצועי קריאה מעולים**: קריאה אחת מחזירה את כל המידע
- **עקביות מובטחת**: כל העדכונים מתבצעים בטרנזקציה אטומית אחת (Atomic Transaction - פעולה שמתבצעת במלואה או בכלל לא)
- **פשטות מודל**: אין צורך בשאילתות מורכבות

#### חסרונות הטמעה:
- **הגבלת גודל מסמך**: MongoDB מגביל מסמך ל-16MB
- **כפילות מידע**: אותו מידע עשוי להיות מאוחסן במספר מקומות
- **עדכונים מסובכים**: שינוי במידע מוטמע דורש עדכון במספר מקומות

#### דיאגרמת מסמך עם הטמעה מלאה

```mermaid
graph TD
    A[Student Document] -->|contains| B[Embedded Course 1]
    A -->|contains| C[Embedded Course 2]
    A -->|contains| D[Embedded Course 3]
    
    B -->|properties| B1[name]
    B -->|properties| B2[department]
    B -->|properties| B3[grade]
    
    C -->|properties| C1[name]
    C -->|properties| C2[department]
    C -->|properties| C3[grade]
```

#### דוגמת קוד JSON עם הטמעה

</div>

<div dir="ltr">

```json
{
  "_id": "student1",
  "name": "Israel Israeli",
  "email": "israel@example.com",
  "courses": [
    {
      "name": "Mathematics",
      "department": "Exact Sciences",
      "credits": 3,
      "grade": "A"
    },
    {
      "name": "Physics",
      "department": "Exact Sciences",
      "credits": 4,
      "grade": "B+"
    }
  ]
}
```

</div>

<div dir="rtl">

### 2. רפרנסים (References)

שמירת מזהים (IDs - Primary Keys, מפתחות ראשיים ייחודיים המזהים כל רשומה באופן חד-משמעי) של מסמכים קשורים, במקום המסמכים עצמם. זוהי גישה דומה למפתחות זרים בבסיסי נתונים רלציוניים, אך עם גמישות רבה יותר. הגישה מתאימה כאשר המידע הקשור גדול, משתנה לעתים קרובות, או נדרש באופן נפרד.

#### יתרונות רפרנסים:
- **חיסכון במקום**: אין כפילות מידע
- **עדכונים יעילים**: שינוי במסמך אחד משפיע בכל מקום
- **גמישות בשאילתות**: ניתן לשלוף מידע חלקי
- **ללא הגבלת גודל**: כל מסמך יכול לגדול עצמאית

#### חסרונות רפרנסים:
- **ביצועי קריאה איטיים**: נדרשות מספר שאילתות
- **מורכבות ביישום**: דרוש קוד נוסף לחיבור המידע
- **בעיות עקביות**: מידע עלול להיות לא סינכרוני

#### דיאגרמת מסמכים עם רפרנסים

</div>

<div dir="ltr">

```mermaid
graph TD
    A[Student Document] -->|references| B[Course IDs Array]
    B -->|contains| B1[course1]
    B -->|contains| B2[course2]
    
    C[Course Document 1] -->|has id| B1
    D[Course Document 2] -->|has id| B2
    
    C -->|references| E[Student IDs Array]
    D -->|references| E
    
    E -->|contains| E1[student1]
    E -->|contains| E2[student2]
```
</div>

<div dir="rtl">

#### דוגמת קוד JSON עם רפרנסים

</div>

<div dir="ltr">

// Student document
```json

{
  "_id": "student1",
  "name": "Israel Israeli",
  "email": "israel@example.com",
  "courseIds": ["course1", "course2"]
}
```
// Course document
```json


{
  "_id": "course1",
  "name": "Mathematics",
  "department": "Exact Sciences",
  "credits": 3,
  "studentIds": ["student1", "student3", "student4"]
}
```

</div>

<div dir="rtl">

### 3. קולקציית קשר (Relationship Collection)

שימוש בקולקציה נפרדת לניהול הקשרים, בדומה לטבלת קשר ב-SQL (Junction Table - טבלה מתווכת המקשרת בין שתי טבלאות ביחס רבים-לרבים). גישה זו מתאימה במיוחד כאשר לקשר עצמו יש מידע נוסף (כמו ציון, תאריך הרשמה, סטטוס) או כאשר המערכת מורכבת ונדרשת גמישות מקסימלית.

#### יתרונות קולקציית קשר:
- **מידע עשיר על הקשר**: אפשרות לשמור מטא-דאטה (Meta-data - מידע על המידע, כמו תאריכים, סטטוסים ופרמטרים נוספים) על הקשר
- **שאילתות מתקדמות**: אפשרות לחפש לפי מאפייני הקשר
- **ביצועים מאוזנים**: עדכונים מהירים, קריאות בינוניות
- **גמישות מקסימלית**: ניתן להוסיף מידע בעתיד

#### חסרונות קולקציית קשר:
- **מורכבות גבוהה**: דורש ניהול שלוש קולקציות
- **שאילתות מורכבות**: נדרש קוד מתקדם לחיבור הנתונים
- **ביצועי קריאה איטיים**: נדרשות מספר שאילתות לכל אחזור

#### דיאגרמת מודל עם קולקציית קשר

```mermaid
graph TD
    A[Student Collection] --- C[Enrollment Collection]
    B[Course Collection] --- C
    
    C -->|contains| D[Enrollment Document 1]
    C -->|contains| E[Enrollment Document 2]
    
    D -->|has| D1[studentId: student1]
    D -->|has| D2[courseId: course1]
    D -->|has| D3[grade: A]
    
    E -->|has| E1[studentId: student1]
    E -->|has| E2[courseId: course2]
    E -->|has| E3[grade: B+]
```

#### דוגמת קוד JSON עם קולקציית קשר
// Student document

</div>

<div dir="ltr">

```json

{
  "_id": "student1",
  "name": "Israel Israeli",
  "email": "israel@example.com"
}
```

// Course document
```json
{
  "_id": "course1",
  "name": "Mathematics",
  "department": "Exact Sciences",
  "credits": 3
}
```

// Enrollment document (relationship collection)
```json
{
  "_id": "enrollment1",
  "studentId": "student1",
  "courseId": "course1",
  "enrollmentDate": "2025-01-15",
  "grade": "A",
  "status": "ACTIVE"
}
```

</div>

<div dir="rtl">

## אסטרטגיות מתקדמות

### 1. הטמעה סלקטיבית (Selective Embedding)

שילוב של הטמעה ורפרנסים - הטמעת מידע בסיסי והפניה למידע מפורט. גישה זו מאפשרת לשלב את היתרונות של שתי השיטות: ביצועי קריאה מהירים למידע הבסיסי, ואפשרות לטעון מידע מפורט בעת הצורך. מתאימה במיוחד כאשר יש מידע שנדרש לעתים קרובות (כמו שם) ומידע שנדרש לעתים רחוקות (כמו פרטים מלאים).

```mermaid
graph TD
    A[Student Document] -->|contains| B[Courses Array]
    
    B -->|contains| C[Course Reference 1]
    B -->|contains| D[Course Reference 2]
    
    C -->|has| C1[courseId: course1]
    C -->|has| C2[name: Mathematics]
    C -->|has| C3[grade: A]
    
    D -->|has| D1[courseId: course2]
    D -->|has| D2[name: Physics]
    D -->|has| D3[grade: B+]
    
    E[Complete Course Document 1] -.->|referenced by| C
    F[Complete Course Document 2] -.->|referenced by| D
```

### 2. גישה מבוססת תרחישי שימוש (Usage Pattern Based)

בחירת האסטרטגיה על בסיס ניתוח דפוסי הגישה למידע במערכת. זוהי גישה פרגמטית שמתבססת על מדידות ביצועים בפועל ולא על עקרונות תיאורטיים בלבד.

#### שיקולים לניתוח דפוסי שימוש:
- **תדירות קריאה vs כתיבה**: האם המערכת קוראת הרבה או כותבת הרבה?
- **כיוון הגישה**: האם נגישים בעיקר לסטודנטים או לקורסים?
- **גודל התוצאות**: כמה נתונים בדרך כלל מוחזרים?
- **זמני תגובה נדרשים**: האם נדרשת תגובה מיידית?

```mermaid
flowchart TD
    A[Analyze Access Patterns] --> B{Query Type?}
    
    B -->|Read Student + Courses| C[Embed Courses in Student]
    B -->|Read Course + Students| D[Embed Students in Course]
    B -->|Balanced/Complex| E[Use References + Indexes]
    B -->|Need Additional Data| F[Relationship Collection]
    
    C --> G[Optimize for Student Access]
    D --> H[Optimize for Course Access]
    E --> I[Flexible for Both Directions]
    F --> J[Complex Relationship Data]
```

## השוואת זמני ביצוע ושיקולי ביצועים

הבנת ההשפעה של כל אסטרטגיה על ביצועי המערכת חיונית לקבלת החלטות נכונות:

### ביצועי קריאה (Read Performance):
- **הטמעה**: מהירה ביותר - שאילתה אחת
- **רפרנסים**: בינונית - מספר שאילתות
- **קולקציית קשר**: איטית יחסית - שאילתות מורכבות

### ביצועי כתיבה (Write Performance):
- **הטמעה**: איטית לעדכונים נפוצים - צריך לעדכן במספר מקומות
- **רפרנסים**: מהירה - עדכון במקום אחד
- **קולקציית קשר**: מהירה מאוד - עדכון של רשומת קשר בלבד

### צריכת זיכרון (Memory Usage):
- **הטמעה**: גבוהה - כפילות מידע
- **רפרנסים**: נמוכה - אין כפילות
- **קולקציית קשר**: בינונית - מידע נוסף על הקשרים

```mermaid
graph LR
    A[Query Type] --> B[Embedded Documents]
    A --> C[References]
    A --> D[Relationship Collection]
    
    B -->|Read Parent with Children| B1[Very Fast ]
    B -->|Update Single Child| B2[Fast ]
    B -->|Update Multiple Children| B3[Slow ]
    
    C -->|Read Parent with Children| C1[Medium ]
    C -->|Update Single Child| C2[Medium ]
    C -->|Update Multiple Children| C3[Fast ]
    
    D -->|Read Parent with Children| D1[Slow ]
    D -->|Update Single Child| D2[Fast ]
    D -->|Update Multiple Children| D3[Fast ]
```

## שיקולי מדרגיות (Scalability Considerations)

### הטמעה ומדרגיות:
- **בעיה**: כאשר המערכת גדלה, המסמכים עלולים להגיע למגבלת ה-16MB
- **פתרון**: מעבר להטמעה סלקטיבית או רפרנסים

### רפרנסים ומדרגיות:
- **יתרון**: כל קולקציה יכולה לגדול עצמאית
- **אתגר**: נדרשים אינדקסים (Indexes - מבני נתונים המאיצים חיפושים) יעילים

### קולקציית קשר ומדרגיות:
- **יתרון**: מדרגיות מעולה - כל חלק גדל עצמאית
- **אתגר**: נדרש שרדינג (Sharding - חלוקת נתונים על שרתים מרובים) מתוחכם

## המלצות ליישום

### תרשים החלטה לבחירת אסטרטגיה

```mermaid
flowchart TD
    A[Start] --> B{Relationship Type?}
    
    B -->|One-to-One| C[Consider Embedding]
    B -->|One-to-Few| D[Consider Embedding]
    B -->|One-to-Many| E{How Many?}
    B -->|Many-to-Many| F{Additional Info?}
    
    E -->|Few Items < 100| D
    E -->|Many Items > 100| G[Use References]
    
    F -->|Yes - Grades, Dates, Status| H[Use Relationship Collection]
    F -->|No - Simple Connection| I[Use Two-Way References]
    
    C -->|Small Documents < 1KB| C1[Embed]
    C -->|Large Documents > 1KB| C2[Reference]
    
    D --> J{Doc Size Growth?}
    J -->|Grows Unbounded| G
    J -->|Limited Growth < 16MB| K[Embed]
```

### המלצות ספציפיות לפי תרחיש:

#### מערכת בלוג:
- **מחבר ↔ פוסטים**: רפרנסים (פוסט אחד למחבר אחד, אך מחבר יכול לכתוב פוסטים רבים)
- **פוסט ↔ תגובות**: הטמעה (תגובות נקראות תמיד עם הפוסט)
- **משתמש ↔ לייקים**: קולקציית קשר (נדרש זמן הלייק, מספר הלייקים)

#### מערכת מסחר אלקטרוני:
- **לקוח ↔ הזמנות**: רפרנסים (הזמנות רבות ללקוח)
- **הזמנה ↔ פריטים**: הטמעה (פריטי הזמנה נקראים תמיד יחד)
- **מוצר ↔ קטגוריות**: רפרנסים דו-כיווניים (מוצר יכול להיות במספר קטגוריות)

#### מערכת רשת חברתית:
- **משתמש ↔ פרופיל**: הטמעה (פרופיל נקרא תמיד עם המשתמש)
- **משתמש ↔ חברים**: קולקציית קשר (נדרש תאריך חברות, סטטוס קשר)
- **משתמש ↔ פוסטים**: רפרנסים (פוסטים רבים למשתמש)

## ארכיטקטורה מלאה של יחסי רבים-לרבים

דיאגרמה המציגה את המבנה המלא של מערכת הרישום לקורסים עם כל השיקולים:

```mermaid
graph TD
    subgraph "Student Collection"
        A1[Student 1] -->|has| A2[basic info: name, email, year]
        A1 -->|references| A3[courseIds: quick access]
        A1 -->|has| A4[indexes on: email, year]
    end
    
    subgraph "Course Collection"
        B1[Course 1] -->|has| B2[basic info: name, department, credits]
        B1 -->|references| B3[studentIds: quick access]
        B1 -->|has| B4[indexes on: department, credits]
    end
    
    subgraph "Enrollment Collection"
        C1[Enrollment 1] -->|connects| C2[studentId + courseId]
        C1 -->|contains| C3[grade, status, enrollmentDate]
        C1 -->|has| C4[compound index on studentId + courseId - אינדקס מורכב על שני שדות יחד לחיפוש מהיר]
    end
    
    A3 -.->|refers to| B1
    B3 -.->|refers to| A1
    C2 -.->|refers to| A1
    C2 -.->|refers to| B1
    
    subgraph "Performance Optimizations"
        D1[Aggregation Pipeline for complex queries - צינור עיבוד לשאילתות מורכבות עם מספר שלבים]
        D2[Background indexing - יצירת אינדקסים ברקע מבלי לחסום את המערכת]
        D3[Caching frequently accessed data - שמירת נתונים נפוצים בזיכרון מהיר]
    end
```

## זרימת נתונים בעת רישום סטודנט לקורס

תהליך מפורט הכולל אימותים ואופטימיזציות:

```mermaid
sequenceDiagram
    participant Client
    participant API
    participant Cache
    participant DB as MongoDB
    participant Index as Index Manager
    
    Client->>API: POST /enrollments/student/1/course/2
    
    API->>Cache: Check student cache
    alt Student in cache
        Cache-->>API: Return cached student
    else Student not in cache
        API->>DB: Find Student ID:1 with index
        DB-->>API: Return Student
        API->>Cache: Cache student data
    end
    
    API->>DB: Find Course ID:2 with index
    DB-->>API: Return Course
    
    API->>DB: Check existing enrollment (compound index)
    DB-->>API: No enrollment found
    
    Note over API,DB: Transaction Start (Transactions - מנגנון המבטיח שמספר פעולות יתבצעו יחד או לא יתבצעו כלל)
    API->>DB: Create Enrollment Document
    DB-->>API: Return Enrollment ID
    
    API->>DB: Update Student.courseIds (atomic)
    API->>DB: Update Course.studentIds (atomic)
    API->>Index: Update secondary indexes
    Note over API,DB: Transaction Commit
    
    API->>Cache: Invalidate affected cache entries
    API-->>Client: Return Enrollment Data
```

## שיקולי ביצועים מתקדמים

### אינדקסים אסטרטגיים:
```mermaid
graph TD
    A[Index Strategy] --> B[Single Field Indexes]
    A --> C[Compound Indexes - אינדקסים על מספר שדות יחד]
    A --> D[Text Indexes - אינדקסים לחיפוש טקסט מלא בתוכן]
    A --> E[Geospatial Indexes - אינדקסים למיקומים גיאוגרפיים]
    
    B --> B1[Primary: _id]
    B --> B2[Unique: email]
    B --> B3[Regular: department]
    
    C --> C1[studentId + courseId]
    C --> C2[department + credits]
    C --> C3[enrollmentDate + status]
    
    D --> D1[Course name and description - חיפוש טקסט בשדות תוכן]
    
    E --> E1[Student location for campus courses - מיקום גיאוגרפי לקורסים אזוריים]
```

### אופטימיזציות שאילתות:
- **Aggregation Pipeline**: לשאילתות מורכבות הכוללות חישובים
- **$lookup**: לחיבור נתונים מקולקציות שונות (כמו JOIN - פעולת חיבור טבלאות בSQL)
- **$facet**: לביצוע מספר שאילתות במקביל (חלוקת השאילתה למספר כיוונים שונים)
- **Read Preferences**: לחלוקת עומס בין replicas (עותקים של בסיס הנתונים על שרתים שונים)

### ניהול זיכרון:
- **Working Set**: וידוא שהנתונים הפעילים נמצאים בזיכרון (חלק מהנתונים שנגיש באופן תכוף ונשמר ב-RAM למהירות גבוהה)
- **Connection Pooling**: ניהול יעיל של חיבורי מסד הנתונים (שיתוף חיבורים קיימים במקום יצירת חיבורים חדשים כל פעם)
- **Caching Strategy**: שכבת cache למידע שנגיש לעתים קרובות



בחירת האסטרטגיה הנכונה למימוש יחסים ב-MongoDB תלויה בגורמים רבים ומורכבים:

### גורמים טכניים:
1. **סוג היחס** (אחד-לאחד, אחד-לרבים, רבים-לרבים)
2. **דפוס הגישה** (כיוון הקריאה הנפוץ ביותר, יחס קריאה/כתיבה)
3. **היקף המידע** (כמות המסמכים, גודל ממוצע, קצב גידול)
4. **דרישות ביצועים** (זמני תגובה, throughput נדרש - כמות הפעולות לשנייה שהמערכת צריכה לטפל בהן)
5. **מדרגיות נדרשת** (האם המערכת צפויה לגדול משמעותית)

### גורמים עסקיים:
1. **קצב השינויים** (תדירות העדכונים, יציבות המודל)
2. **דרישות עקביות** (מיידית, אוונטואלית, או לפי context)
3. **מורכבות פיתוח** (זמן לשוק, מומחיות הצוות)
4. **עלויות תפעול** (משאבי שרת, רישיונות, תחזוקה)

### עקרונות מנחים:
- **התחל פשוט**: בחר בגישה הפשוטה ביותר שעונה על הצרכים
- **מדוד ואופטימז**: השתמש בכלי מוניטורינג לבחינת ביצועים בפועל
- **תכנן לעתיד**: קח בחשבון את הגידול הצפוי במערכת
- **שמור על גמישות**: אפשר מעבר בין אסטרטגיות בעתיד

אין פתרון "אחד מתאים לכולם", וחשוב לבחור את האסטרטגיה המתאימה ביותר לדרישות הספציפיות של המערכת, תוך התחשבות בכל הגורמים הרלוונטיים. במקרים רבים, שילוב של מספר אסטרטגיות באותה מערכת (Hybrid Approach - גישה משולבת המשתמשת בשיטות שונות לחלקים שונים של המערכת) מספק את התוצאה האופטימלית.

## מושגים  הסברים עבורם:

### מושגי בסיס:
- **Foreign Keys** - עמודות המכילות מזהים שמקשרים בין טבלאות שונות
- **Primary Keys** - מפתחות ראשיים ייחודיים המזהים כל רשומה באופן חד-משמעי
- **Junction Table** - טבלה מתווכת המקשרת בין שתי טבלאות ביחס רבים-לרבים

### מושגי טרנזקציות ועקביות:
- **Atomic Transaction** - פעולה שמתבצעת במלואה או בכלל לא
- **Transactions** - מנגנון המבטיח שמספר פעולות יתבצעו יחד או לא יתבצעו כלל
- **Meta-data** - מידע על המידע, כמו תאריכים, סטטוסים ופרמטרים נוספים

### מושגי ביצועים:
- **Throughput** - כמות הפעולות לשנייה שהמערכת צריכה לטפל בהן
- **Working Set** - חלק מהנתונים שנגיש באופן תכוף ונשמר ב-RAM למהירות גבוהה
- **Connection Pooling** - שיתוף חיבורים קיימים במקום יצירת חיבורים חדשים כל פעם

### מושגי אינדקסים ושאילתות:
- **Compound Index** - אינדקס מורכב על שני שדות יחד לחיפוש מהיר
- **Text Indexes** - אינדקסים לחיפוש טקסט מלא בתוכן
- **Geospatial Indexes** - אינדקסים למיקומים גיאוגרפיים
- **Background indexing** - יצירת אינדקסים ברקע מבלי לחסום את המערכת

### מושגי MongoDB מתקדמים:
- **Aggregation Pipeline** - צינור עיבוד לשאילתות מורכבות עם מספר שלבים
- **$lookup** - פעולת חיבור נתונים (כמו JOIN בSQL)
- **$facet** - חלוקת השאילתה למספר כיוונים שונים
- **Replicas** - עותקים של בסיס הנתונים על שרתים שונים

### מושגי ארכיטקטורה:
- **Hybrid Approach** - גישה משולבת המשתמשת בשיטות שונות לחלקים שונים של המערכת

</div>