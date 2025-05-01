<div dir="rtl">

# תרגיל מעשי: יצירת אפליקציית ספרייה עם MongoDB

## מטרת התרגיל

בתרגיל זה תבנה אפליקציית Spring Boot פשוטה לניהול ספרייה המשתמשת ב-MongoDB כבסיס הנתונים. האפליקציה תאפשר לשמור, לחפש ולנהל ספרים בספרייה.

## דרישות בסיסיות

1. יצירת מודל `Book` (Collection)
2. יצירת Repository עם מספר שאילתות בסיסיות
3. יצירת DTO, Service ו-Controller בסיסיים

## שלב 1: הגדרת המודל

הגדר מחלקה בשם `Book` שתייצג ספר בספרייה. המחלקה צריכה לכלול את השדות הבאים:
- מזהה ייחודי
- כותרת הספר
- שם המחבר
- שנת פרסום
- ז'אנר
- האם זמין להשאלה (boolean)
- מערך של תגיות (tags)
- דירוג (בין 1-5)

**דוגמה חלקית**:

</div>

```java
@Document(collection = "books")
public class Book {
    @Id
    private String id;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    // הוסף את שאר השדות הנדרשים
}
```

<div dir="rtl">

## שלב 2: יצירת Repository

צור ממשק `BookRepository` שיירש מ-`MongoRepository` ויכלול שלוש שאילתות פשוטות:

1. מציאת ספרים לפי מחבר
2. מציאת ספרים זמינים לפי ז'אנר
3. מציאת ספרים לפי תג (חיפוש בתוך מערך)

**רמז**:

</div>

```java
public interface BookRepository extends MongoRepository<Book, String> {
    // שיטה למציאת ספרים לפי מחבר
    List<Book> findByAuthor(String author);
    
    // כתוב את שתי השיטות הנוספות
}
```

<div dir="rtl">

## שלב 3: יצירת DTO

צור מחלקת DTO בשם `BookDto` שתיהיה מקבילה למחלקת `Book` ותשמש להעברת מידע בין השכבות.

## שלב 4: יצירת Mapper

צור מחלקת `BookMapper` שתהיה אחראית על המרה בין `Book` ל-`BookDto` ולהיפך.

**דוגמה חלקית**:

</div>

```java
@Component
public class BookMapper {
    public Book toEntity(BookDto dto) {
        // המר את ה-DTO לאובייקט Book
    }
    
    public BookDto toDto(Book entity) {
        // המר את אובייקט Book ל-DTO
    }
}
```

<div dir="rtl">

## שלב 5: יצירת Service

צור ממשק `BookService` ומחלקת מימוש `BookServiceImpl` שתספק פעולות בסיסיות כמו:
- קבלת כל הספרים
- קבלת ספר לפי מזהה
- יצירת ספר חדש
- עדכון ספר קיים
- מחיקת ספר
- שיטות המשתמשות בשאילתות שיצרת ב-Repository

## שלב 6: יצירת Controller

צור מחלקת `BookController` שתחשוף את הפונקציונליות דרך REST API.

**דוגמה**:
</div>

```java
@RestController
@RequestMapping("/api/books")
public class BookController {
    // הוסף את הנדרש
    
    @GetMapping("/author/{author}")
    public ResponseEntity<List<BookDto>> getBooksByAuthor(@PathVariable String author) {
        // קוד לקבלת ספרים לפי מחבר
    }
    
    // הוסף את שאר ה-endpoints
}
```

<div dir="rtl">

## שלב 7: טיפול בשגיאות

צור מחלקה `GlobalExceptionHandler` שתטפל בחריגות כמו:
- `ResourceNotFoundException`
- שגיאות ולידציה
- שגיאות כלליות

## שלב 8: הגדרת MongoDB

הגדר את החיבור ל-MongoDB בקובץ `application.properties`.
</div>

```properties
# MongoDB Configuration
spring.data.mongodb.authentication-database=admin
spring.data.mongodb.database=library
spring.data.mongodb.host=localhost

#your MongoDB host port
spring.data.mongodb.port=27017

#your MongoDB username and password
spring.data.mongodb.username=admin
spring.data.mongodb.password=admin
```

<div dir="rtl">

## שלב ב': הרחבת השאילתות (לאחר למידת MongoDB Query Language)

לאחר שסיימת את הפרויקט הבסיסי ולמדת על שפת השאילתות של MongoDB, הוסף את השאילתות הבאות ל-Repository:

1. הוסף שאילתה שמחזירה ספרים משנת פרסום מסוימת ומעלה (השתמש באופרטור `$gte`)
2. הוסף שאילתה שמחפשת ספרים לפי חלק מהכותרת ללא תלות ברישיות (השתמש באופרטורים `$regex` ו-`$options`)
3. הוסף שאילתת MongoDB Query מותאמת אישית שמחזירה ספרים עם דירוג מינימלי מסוים שזמינים להשאלה

**רמז לשאילתה מותאמת אישית**:

</div>

```java
@Query("{ 'rating': { $gte: ?0 }, 'available': true }")
List<Book> findAvailableBooksWithMinRating(Integer minRating);
```

<div dir="rtl">

**הסבר:**
- `$gte` - אופרטור השוואה "גדול או שווה ל-" (greater than or equal)
- `?0` - מייצג את הפרמטר הראשון שמועבר למתודה (minRating)
- השאילתה מחפשת מסמכים שבהם הדירוג גדול או שווה למספר שמועבר כפרמטר וגם זמינים להשאלה

## תרגול קצר: הסבר האופרטורים ב-MongoDB

הסבר בקצרה מה עושה כל אחד מהאופרטורים הבאים ב-MongoDB:

1. `$eq` - אופרטור שוויון (equals): בודק האם ערך שדה שווה לערך מסוים
2. `$ne` - אופרטור אי-שוויון (not equals): בודק האם ערך שדה אינו שווה לערך מסוים
3. `$gt` - אופרטור "גדול מ-" (greater than): בודק האם ערך שדה גדול מערך מסוים
4. `$lt` - אופרטור "קטן מ-" (less than): בודק האם ערך שדה קטן מערך מסוים
5. `$in` - בודק האם ערך שדה נמצא ברשימת ערכים מוגדרת
6. `$regex` - מאפשר חיפוש באמצעות ביטויים רגולריים (חיפוש תבניות בטקסט)
7. `$exists` - בודק האם שדה מסוים קיים במסמך

**דוגמה לשימוש:**
</div>

```java
// חיפוש ספרים עם ז'אנר שהוא או "מדע בדיוני" או "פנטזיה"
@Query("{ 'genre': { $in: ['Science Fiction', 'Fantasy'] } }")
List<Book> findSciFiOrFantasyBooks();

// חיפוש ספרים שהכותרת שלהם מתחילה ב"הארי" (ללא תלות ברישיות)
@Query("{ 'title': { $regex: '^הארי', $options: 'i' } }")
List<Book> findBooksTitleStartsWithHarry();
```

<div dir="rtl">

## בדיקת האפליקציה

כדי לבדוק את האפליקציה, השתמש ב-Postman או כלי דומה לשליחת בקשות:

1. צור מספר ספרים באמצעות POST
2. בדוק את השאילתות השונות באמצעות GET
3. עדכן ספר באמצעות PUT
4. מחק ספר באמצעות DELETE

## אתגר נוסף

הוסף אפשרות לחיפוש מתקדם של ספרים לפי שילוב של מספר תנאים (למשל: מחבר, טווח שנים, וזמינות).

**רמז:**
1. צור מחלקת DTO חדשה שתייצג את פרמטרי החיפוש:

</div>

```java
@Data
public class BookSearchCriteria {
    private String author;
    private Integer publishYearFrom;
    private Integer publishYearTo;
    private String genre;
    private Boolean available;
    private String titleContains;
    private Integer minRating;
    // אפשר להוסיף עוד שדות לפי הצורך
}
```

<div dir="rtl">

2. הוסף שאילתה מותאמת אישית ב-Repository שמשתמשת באופרטורים הלוגיים `$and` ו-`$or` לבניית שאילתה דינמית:
</div>

```java
@Query("{ $and: [ ?0, ?1, ?2, ?3 ] }")
List<Book> findBooksByCriteria(Document authorCriteria, 
                              Document yearCriteria,
                              Document genreCriteria,
                              Document availabilityCriteria);
```

<div dir="rtl">

3. צור מתודה בשירות שתבנה את קריטריוני החיפוש בצורה דינמית על פי השדות שמולאו ב-DTO.

בונוס: אפשר להשתמש ב-MongoTemplate ליצירת שאילתות דינמיות עוד יותר מורכבות.

</div>