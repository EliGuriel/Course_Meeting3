<div dir="rtl">

# סיכום Stage 3 - קשר רבים לרבים (Many-to-Many) ב-MongoDB

## 1. מבנה נתונים וקשרים
במודול Stage 3 יישמנו קשר רבים-לרבים (many-to-many) בין סטודנטים (Students) וקורסים (Courses) באמצעות MongoDB:

* **Student** - מודל המייצג סטודנט עם פרטים אישיים וציונים
* **Course** - מודל המייצג קורס אקדמי עם פרטי הקורס
* **Enrollment** - מודל ביניים המייצג את הרישום של סטודנט לקורס (טבלת קשר)
* **יחס רבים לרבים** - כל סטודנט יכול להירשם למספר קורסים, וכל קורס יכול לכלול מספר סטודנטים

## 2. אסטרטגיית היישום

בחרנו ביישום הבא עבור הקשר רבים-לרבים:

1. **רפרנסים הדדיים** - רשימת מזהים של קורסים בכל סטודנט, ורשימת מזהים של סטודנטים בכל קורס
2. **מודל ביניים מפורש** - שימוש ב-`Enrollment` כמודל נפרד לשמירת מידע נוסף על הקשר (תאריך רישום, ציון, סטטוס)
3. **שמירת עקביות** - שינויים בקשר מתעדכנים בכל המודלים המעורבים (בטרנזקציות)

## 3. מרכיבי המערכת

### מודלים (Collections)
* **Student** - סטודנט עם רשימת `courseIds`
* **Course** - קורס עם רשימת `studentIds`
* **Enrollment** - רישום עם `studentId`, `courseId` ופרטים נוספים

### DTOs
* **StudentDto** / **CourseDto** / **EnrollmentDto** - מודלים להעברת נתונים
* **BasicStudentDto** / **BasicCourseDto** - גרסאות מצומצמות לשימוש ביחסים
* **StudentWithCoursesDto** / **CourseWithStudentsDto** - מודלים משולבים

### Mappers
* **StudentMapper** / **CourseMapper** / **EnrollmentMapper**

### Repositories
* **StudentRepository** / **CourseRepository** / **EnrollmentRepository**
* תמיכה בשאילתות מורכבות והרחבת פונקציונליות

### Services
* **StudentService** - ניהול סטודנטים
* **CourseService** - ניהול קורסים
* **EnrollmentService** - ניהול הרישום ויחסי הביניים

### Controllers
* **StudentController** / **CourseController** / **EnrollmentController**

### טיפול בשגיאות
* **GlobalExceptionHandler** - טיפול מרוכז בשגיאות
* חריגות מותאמות אישית לסוגי שגיאות שונים

## 4. יתרונות הגישה

1. **גמישות** - אפשרות לשמור מידע נוסף על הקשר (ציונים, סטטוס, הערות)
2. **יעילות בשאילתות** - אפשרות לשלוף קורסים של סטודנט או סטודנטים של קורס במהירות
3. **אטומיות** - שימוש בטרנזקציות לשמירה על עקביות נתונים
4. **הפרדת אחריות** - חלוקה ברורה בין שכבות המערכת

## 5. דוגמאות לפעולות על הקשר רבים-לרבים

### רישום סטודנט לקורס
```http
POST /api/enrollments/student/{studentId}/course/{courseId}
```

### ביטול רישום סטודנט מקורס
```http
DELETE /api/enrollments/student/{studentId}/course/{courseId}
```

### עדכון ציון לסטודנט בקורס
```http
PUT /api/enrollments/student/{studentId}/course/{courseId}/grade?grade=A&score=95.5
```

### קבלת כל הקורסים של סטודנט
```http
GET /api/students/{id}/with-courses
```

### קבלת כל הסטודנטים של קורס
```http
GET /api/courses/{id}/with-students
```

## 6. הערות יישום

* **טרנזקציות** - נדרשת MongoDB 4.0 ומעלה לתמיכה בטרנזקציות
* **ולידציה** - שימוש ב-Bean Validation API לאימות הנתונים
* **אינדקסים** - יצירת אינדקסים על שדות כמו `email` בסטודנט
* **DTOs** - הפרדה בין המודל הפנימי למודל החיצוני

## סיכום

מודול Stage 3 מדגים כיצד לממש קשר רבים-לרבים אפקטיבי בסביבת MongoDB, תוך שמירה על עקרונות תכנות טובים ועקביות נתונים. האסטרטגיה שנבחרה מאזנת בין ביצועים לבין גמישות, ומאפשרת ניהול יעיל של הקשר המורכב.
</div>