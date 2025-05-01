
<div dir="rtl">

# השוואה בין MongoDB ו-JPA/JPQL

## מבוא

כאשר מפתחים יישומי Spring Boot, אחת ההחלטות החשובות היא בחירת בסיס הנתונים והטכנולוגיה לעבודה מולו. שתי אפשרויות פופולריות הן:
- **JPA (Java Persistence API)** עם בסיסי נתונים רלציוניים כמו MySQL/PostgreSQL
- **MongoDB** כבסיס נתונים NoSQL מבוסס מסמכים

מסמך זה מסכם את ההבדלים העיקריים בדגש על אופן כתיבת השאילתות וה-Repositories בכל אחת מהטכנולוגיות.

</div>

## הגדרת ישויות (Entities/Documents)

<div dir="rtl">

### מסמכים ב-MongoDB

</div>

```java
@Data
@Document(collection = "person")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Person {
    @Id
    private String personId;
    
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    private Integer age;
    
    @NotEmpty(message = "Person must have at least one hobby")
    private List<String> hobbies;
    
    private List<Address> addresses;
    
    @Indexed(unique = true)
    private String email;
}
```

<div dir="rtl">

## הבדלים בסיסיים

| JPA | MongoDB |
|-----|---------|
| שימוש ב-@Entity | שימוש ב-@Document |
| @Table לשם טבלה | @Document(collection = "name") |
| @Id עם @GeneratedValue | @Id לרוב String (אך אין חובה ב-generator) |
| @Column עם מאפיינים | אין צורך באנוטציות לשדות רגילים |
| יחסים מוגדרים: @OneToMany, @ManyToOne וכו' | יחסים מיוצגים על ידי מזהים (personId) או הטמעה |
| הכל מנורמל בין טבלאות | הכל מוטמע במסמך אחד או מקושר דרך ID |

</div>

## הגדרת Repositories

<div dir="rtl">

### MongoDB Repositories

</div>

```java
@Repository
public interface PersonRepository extends MongoRepository<Person, String> {
    Optional<Person> findByEmail(String email);
    
    List<Person> findByLastName(String lastName);
    
    List<Person> findByAgeGreaterThan(int age);
    
    @Query("{ 'addresses.city': ?0 }")
    List<Person> findByAddressCity(String city);
    
    @Query("{ 'hobbies': ?0 }")
    List<Person> findByHobby(String hobby);
}
```

<div dir="rtl">

## שפת שאילתות: JPQL מול MongoDB Query

</div>

### MongoDB Query Language

<div dir="rtl">

שאילתות MongoDB משתמשות במבנה דמוי JSON.

**מאפיינים חשובים:**
- השאילתות מבוססות על זוגות key-value
- ניתן לשאול על שדות מקוננים באמצעות נקודה (`.`)
- שימוש באופרטורים כמו `$gt`, `$lt`, `$in` וכו'

</div>

```java
// שליפת כל האנשים
"{}"
// הסבר: שאילתה ריקה מחזירה את כל המסמכים באוסף

// שליפה עם תנאי
"{ 'age': { $gt: 18 } }"
// הסבר: $gt = greater than - מחזיר מסמכים שבהם הגיל גדול מ-18

// שאילתה עם שדה מקונן
"{ 'addresses.city': 'Tel Aviv' }"
// הסבר: גישה לשדה מקונן באמצעות נקודה - מחזיר מסמכים שבהם יש כתובת בתל אביב

// שאילתה על ערך במערך
"{ 'hobbies': 'Reading' }"
// הסבר: מחפש מסמכים שבהם המערך 'hobbies' מכיל את הערך 'Reading'

// שאילתה עם מספר תנאים
"{ 'age': { $gt: 20 }, 'lastName': 'Cohen' }"
// הסבר: חיפוש עם מספר תנאים (AND לוגי) - מחזיר מסמכים שבהם הגיל גדול מ-20 וגם שם המשפחה הוא Cohen
```

<div dir="rtl">

## השוואה בין שאילתות נפוצות

</div>
<div dir="rtl">

| תיאור | JPQL | MongoDB Query | הסבר MongoDB |
|-------|------|---------------|--------------|
| מציאת ישות לפי ID | `SELECT s FROM Student s WHERE s.id = :id` | `{ '_id': ?0 }` | חיפוש לפי שדה ה-ID הייחודי של MongoDB |
| מציאת ישויות לפי שדה | `SELECT s FROM Student s WHERE s.lastName = :lastName` | `{ 'lastName': ?0 }` | חיפוש מסמכים לפי ערך מדויק בשדה |
| מציאת ישויות לפי שדה חלקי | `SELECT s FROM Student s WHERE s.lastName LIKE :pattern` | `{ 'lastName': { $regex: ?0, $options: 'i' } }` | `$regex` מאפשר חיפוש טקסט עם תבנית, `i` מציין חיפוש ללא תלות ברישיות |
| סינון לפי טווח | `SELECT s FROM Student s WHERE s.age BETWEEN :min AND :max` | `{ 'age': { $gte: ?0, $lte: ?1 } }` | `$gte` = גדול או שווה, `$lte` = קטן או שווה |
| סינון ערכים חסרים | `SELECT s FROM Student s WHERE s.email IS NULL` | `{ 'email': { $exists: false } }` | `$exists: false` בודק האם השדה לא קיים במסמך |
| מציאת ישויות מקושרות | `SELECT s FROM Student s JOIN s.teachers t WHERE t.id = :id` | `{ 'teachers': ?0 }` או שאילתה נפרדת | כשמדובר במערך, ניתן לחפש ערך ספציפי במערך |
| מציאת ישויות ללא קשרים | `SELECT s FROM Student s LEFT JOIN s.teachers t WHERE t IS NULL` | `{ 'personId': null }` | חיפוש מסמכים שבהם השדה הוא null או לא קיים |



## המרת שאילתות נפוצות

</div>

### חיפוש בסיסי

<div dir="rtl">

**MongoDB:**

</div>

```java
// במתודה:
List<Person> findByLastName(String lastName);
// הסבר: Spring Data מתרגם אוטומטית למבנה { 'lastName': ? }

// או באמצעות @Query:
@Query("{ 'lastName': ?0 }")
List<Person> findByLastName(String lastName);
// הסבר: חיפוש מסמכים שבהם שדה lastName שווה לפרמטר הראשון בשיטה (?0)
```

### חיפוש מתקדם

<div dir="rtl">


**MongoDB:**

</div>

```java
@Query("{ 'age': { $gt: ?0 }, 'addresses.city': { $regex: ?1, $options: 'i' } }")
List<Person> findPersonsByAgeAndCity(int age, String city);
// הסבר: 
// $gt - גדול מ (greater than)
// $regex - חיפוש באמצעות ביטוי רגולרי
// $options: 'i' - התעלמות מרישיות (case insensitive)
// השאילתה מחפשת מסמכים שבהם הגיל גדול מהפרמטר הראשון וגם שדה העיר בכתובות מכיל את הפרמטר השני
```

### חיפוש ביחסים

<div dir="rtl">

**MongoDB:**
בדרך כלל מבוצע בשתי שאילתות או מיוצג באמצעות מזהים

</div>

```java
@Query("{ 'personId': null }")
List<Project> findProjectsWithoutPerson();
// הסבר: מחזיר פרויקטים שבהם שדה personId הוא null - כלומר פרויקטים שלא משויכים לאף אדם
```

<div dir="rtl">

## אופרטורים נפוצים ב-MongoDB

MongoDB מספקת מגוון רחב של אופרטורים לחיפוש וסינון נתונים:

### אופרטורי השוואה
| אופרטור | משמעות | דוגמה | הסבר |
|---------|---------|-------|-------|
| `$eq` | שווה | `{ age: { $eq: 25 } }` | מחזיר מסמכים שבהם הגיל שווה ל-25 |
| `$ne` | לא שווה | `{ status: { $ne: 'ACTIVE' } }` | מחזיר מסמכים שבהם הסטטוס אינו 'ACTIVE' |
| `$gt` | גדול מ | `{ age: { $gt: 18 } }` | מחזיר מסמכים שבהם הגיל גדול מ-18 |
| `$gte` | גדול או שווה | `{ age: { $gte: 18 } }` | מחזיר מסמכים שבהם הגיל גדול או שווה ל-18 |
| `$lt` | קטן מ | `{ age: { $lt: 30 } }` | מחזיר מסמכים שבהם הגיל קטן מ-30 |
| `$lte` | קטן או שווה | `{ age: { $lte: 30 } }` | מחזיר מסמכים שבהם הגיל קטן או שווה ל-30 |
| `$in` | בתוך רשימה | `{ status: { $in: ['ACTIVE', 'PENDING'] } }` | מחזיר מסמכים שבהם הסטטוס הוא 'ACTIVE' או 'PENDING' |
| `$nin` | לא בתוך רשימה | `{ status: { $nin: ['DELETED', 'BLOCKED'] } }` | מחזיר מסמכים שבהם הסטטוס אינו 'DELETED' ואינו 'BLOCKED' |

### אופרטורים לוגיים
| אופרטור | משמעות | דוגמה | הסבר |
|---------|---------|-------|-------|
| `$and` | וגם | `{ $and: [ { age: { $gt: 20 } }, { status: 'ACTIVE' } ] }` | מחזיר מסמכים שעומדים בכל התנאים |
| `$or` | או | `{ $or: [ { age: { $lt: 18 } }, { age: { $gt: 65 } } ] }` | מחזיר מסמכים שעומדים באחד התנאים לפחות |
| `$not` | שלילה | `{ age: { $not: { $gt: 20 } } }` | מחזיר מסמכים שלא עומדים בתנאי |
| `$nor` | לא וגם לא | `{ $nor: [ { age: { $lt: 18 } }, { status: 'DELETED' } ] }` | מחזיר מסמכים שלא עומדים באף אחד מהתנאים |

### אופרטורים למערכים
| אופרטור | משמעות | דוגמה | הסבר |
|---------|---------|-------|-------|
| `$all` | מכיל את כל הערכים | `{ hobbies: { $all: ['Reading', 'Sports'] } }` | מחזיר מסמכים שבהם מערך התחביבים מכיל גם קריאה וגם ספורט |
| `$elemMatch` | מתאים לתנאי | `{ addresses: { $elemMatch: { city: 'Tel Aviv', zip: '6100000' } } }` | מחזיר מסמכים שיש בהם כתובת בתל אביב עם מיקוד מסוים |
| `$size` | גודל מערך | `{ hobbies: { $size: 3 } }` | מחזיר מסמכים שיש להם בדיוק 3 תחביבים |

### אופרטורים לטקסט
| אופרטור | משמעות | דוגמה | הסבר |
|---------|---------|-------|-------|
| `$regex` | ביטוי רגולרי | `{ lastName: { $regex: '^C', $options: 'i' } }` | מחזיר מסמכים ששם המשפחה מתחיל באות C (ללא תלות ברישיות) |
| `$text` | חיפוש טקסט מלא | `{ $text: { $search: "spring mongodb" } }` | חיפוש טקסט חופשי (דורש הגדרת אינדקס טקסט) |

### אופרטורים לבדיקת קיום
| אופרטור | משמעות | דוגמה | הסבר |
|---------|---------|-------|-------|
| `$exists` | קיום שדה | `{ email: { $exists: true } }` | מחזיר מסמכים שיש בהם שדה email |
| `$type` | סוג ערך | `{ age: { $type: 'number' } }` | מחזיר מסמכים שבהם הגיל הוא מספר |

## סיכום

שתי הטכנולוגיות מציעות אפשרויות חזקות לעבודה עם נתונים, אך מייצגות פרדיגמות שונות. בעוד JPA מייצגת את עולם הנתונים הרלציוני המסורתי, MongoDB מייצגת גישה מודרנית יותר מבוססת-מסמכים.

**יתרונות JPA:**
- מתאים במיוחד לנתונים מובנים היטב עם סכמה קשיחה
- חזק ביחסים מורכבים בין טבלאות
- טרנזקציות ACID מלאות ותמיכה בשלמות נתונים

**יתרונות MongoDB:**
- גמישות בסכמה ויכולת התאמה מהירה לשינויים
- ביצועים טובים בקריאה וכתיבה בנפחים גדולים
- נוח לעבודה עם מבני נתונים מקוננים ומורכבים
- מגוון אופרטורים עשיר לחיפוש וסינון מתקדם

הבחירה בין השתיים תלויה בדרישות הספציפיות של הפרויקט, במודל הנתונים ובדפוסי השימוש.

</div>