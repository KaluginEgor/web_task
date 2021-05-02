package com.epam.project.model.service.impl;

import com.epam.project.exception.ConnectionException;
import com.epam.project.exception.DaoException;
import com.epam.project.exception.ServiceException;
import com.epam.project.model.dao.AbstractMediaPersonDao;
import com.epam.project.model.entity.MediaPerson;
import com.epam.project.model.entity.OccupationType;
import com.epam.project.model.pool.ConnectionPool;
import com.epam.project.model.service.MediaPersonService;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.testng.Assert.assertEquals;

public class MediaPersonServiceImplTest {
    @Mock
    private AbstractMediaPersonDao mediaPersonDao;
    private MediaPersonService mediaPersonService;
    private MediaPerson mediaPerson1;
    private MediaPerson mediaPerson2;

    @BeforeClass
    public void initialize() {
        MockitoAnnotations.initMocks(this);
        mediaPersonService = MediaPersonServiceImpl.getInstance();
        mediaPerson1 = new MediaPerson();
        mediaPerson1.setId(34);
        mediaPerson1.setFirstName("James");
        mediaPerson1.setSecondName("Cameron");
        mediaPerson1.setBio("James Francis Cameron was born on August 16, 1954 in Kapuskasing, Ontario, Canada. He moved to the United States in 1971. " +
                "The son of an engineer, he majored in physics at California State University before switching to English, and eventually dropping out. " +
                "He then drove a truck to support his screenwriting ambition. He landed his first professional film job as art director, miniature-set builder, " +
                "and process-projection supervisor on Roger Corman's Battle Beyond the Stars (1980) and had his first experience as a director with a two week stint on Piranha II: The Spawning (1981) before being fired.\n" +
                "\n" +
                "He then wrote and directed The Terminator (1984), a futuristic action-thriller starring Arnold Schwarzenegger, " +
                "Michael Biehn and Linda Hamilton. It was a low budget independent film, but Cameron's superb, dynamic direction made it a surprise mainstream " +
                "success and it is now regarded as one of the most iconic pictures of the 1980s. After this came a string of successful, bigger budget science-fiction " +
                "action films such as Aliens (1986), The Abyss (1989) and Terminator 2: Judgment Day (1991). In 1990, Cameron formed his own production company, Lightstorm Entertainment. " +
                "In 1997, he wrote and directed Titanic (1997), a romance epic about two young lovers from different social classes who meet on board the famous ship. " +
                "The movie went on to break all box office records and earned eleven Academy Awards. It became the highest grossing movie of all time until 12 years later," +
                " Avatar (2009), which invented and pioneered 3D film technology, and it went on to beat \"Titanic\", and became the first film to cost two billion dollars until 2019 when Marvel took the record.\n" +
                "\n" +
                "James Cameron is now one of the most sought-after directors in Hollywood. He was formerly married to producer Gale Anne Hurd, who produced several of his films. " +
                "In 2000, he married actress Suzy Amis, who appeared in Titanic, and they have three children.");
        mediaPerson1.setBirthday(LocalDate.parse("1954-08-16"));
        mediaPerson1.setPicture("C:/Epam/pictures/592ee2bb-83b3-4dc9-95e7-b64b068f97ac.jpg.jpg");
        mediaPerson1.setOccupationType(OccupationType.DIRECTOR);
        mediaPerson2 = new MediaPerson();
        mediaPerson2.setId(35);
        mediaPerson2.setFirstName("Leonardo");
        mediaPerson2.setSecondName("DiCaprio");
        mediaPerson2.setBio("Few actors in the world have had a career quite as diverse as Leonardo DiCaprio's. " +
                "DiCaprio has gone from relatively humble beginnings, as a supporting cast member of the sitcom Growing Pains (1985) " +
                "and low budget horror movies, such as Critters 3 (1991), to a major teenage heartthrob in the 1990s, as the hunky lead actor in " +
                "movies such as Romeo + Juliet (1996) and Titanic (1997), to then become a leading man in Hollywood blockbusters, made by internationally" +
                " renowned directors such as Martin Scorsese and Christopher Nolan.\n" +
                "\n" +
                "Leonardo Wilhelm DiCaprio was born November 11, 1974 in Los Angeles, California, the only child of Irmelin DiCaprio (née Indenbirken) " +
                "and former comic book artist George DiCaprio. His father is of Italian and German descent, and his mother, who is German-born, is of German " +
                "and Russian ancestry. His middle name, \"Wilhelm\", was his maternal grandfather's first name. Leonardo's father had achieved minor status as " +
                "an artist and distributor of cult comic book titles, and was even depicted in several issues of American Splendor, the cult semi-autobiographical " +
                "comic book series by the late 'Harvey Pekar', a friend of George's. Leonardo's performance skills became obvious to his parents early on, and after signing " +
                "him up with a talent agent who wanted Leonardo to perform under the stage name \"Lenny Williams\", DiCaprio began appearing on a number of television commercials " +
                "and educational programs.\n" +
                "\n" +
                "DiCaprio began attracting the attention of producers, who cast him in small roles in a number of television series, such as Roseanne " +
                "(1988) and The New Lassie (1989), but it wasn't until 1991 that DiCaprio made his film debut in Critters 3 (1991), a low-budget horror" +
                " movie. While Critters 3 (1991) did little to help showcase DiCaprio's acting abilities, it did help him develop his show-reel, and " +
                "attract the attention of the people behind the hit sitcom Growing Pains (1985), in which Leonardo was cast in the \"Cousin Oliver\"" +
                " role of a young homeless boy who moves in with the Seavers. While DiCaprio's stint on Growing Pains (1985) was very short, as the " +
                "sitcom was axed the year after he joined, it helped bring DiCaprio into the public's attention and, after the sitcom ended, DiCaprio " +
                "began auditioning for roles in which he would get the chance to prove his acting chops.\n" +
                "\n" +
                "Leonardo took up a diverse range of roles in the early 1990s, including a mentally challenged youth in What's " +
                "Eating Gilbert Grape (1993), a young gunslinger in The Quick and the Dead (1995) and a drug addict in one of his most " +
                "challenging roles to date, Jim Carroll in The Basketball Diaries (1995), a role which the late River Phoenix originally " +
                "expressed interest in. While these diverse roles helped establish Leonardo's reputation as an actor, it wasn't until his " +
                "role as Romeo Montague in Baz Luhrmann's Romeo + Juliet (1996) that Leonardo became a household name, a true movie star. " +
                "The following year, DiCaprio starred in another movie about doomed lovers, Titanic (1997), which went on to beat all box " +
                "office records held before then, as, at the time, Titanic (1997) became the highest grossing movie of all time, and cemented " +
                "DiCaprio's reputation as a teen heartthrob. Following his work on Titanic (1997), DiCaprio kept a low profile for a number of years, " +
                "with roles in The Man in the Iron Mask (1998) and the low-budget The Beach (2000) being some of his few notable roles during this period.\n" +
                "\n" +
                "In 2002, he burst back into screens throughout the world with leading roles in Catch Me If You Can (2002) and Gangs of New York (2002), " +
                "his first of many collaborations with director Martin Scorsese. With a current salary of $20 million a movie, DiCaprio is now one of the " +
                "biggest movie stars in the world. However, he has not limited his professional career to just acting in movies, as DiCaprio is a committed " +
                "environmentalist, who is actively involved in many environmental causes, and his commitment to this issue led to his involvement in The 11th " +
                "Hour, a documentary movie about the state of the natural environment. As someone who has gone from small roles in television commercials to one " +
                "of the most respected actors in the world, DiCaprio has had one of the most diverse careers in cinema. DiCaprio continued to defy conventions about" +
                " the types of roles he would accept, and with his career now seeing him leading all-star casts in action thrillers such as The Departed (2006), " +
                "Shutter Island (2010) and Christopher Nolan's Inception (2010), DiCaprio continues to wow audiences by refusing to conform to any cliché about actors.\n" +
                "\n" +
                "In 2012, he played a mustache twirling villain in Django Unchained (2012), and then tragic literary character Jay Gatsby in The Great Gatsby " +
                "(2013) and Jordan Belfort in The Wolf of Wall Street (2013).\n" +
                "\n" +
                "DiCaprio is passionate about environmental and humanitarian causes, having donated $1,000,000 to earthquake relief " +
                "efforts in 2010, the same year he contributed $1,000,000 to the Wildlife Conservation Society.");
        mediaPerson2.setBirthday(LocalDate.parse("1974-11-11"));
        mediaPerson2.setPicture("C:/Epam/pictures/f62a8114-5aff-4469-8123-3ab924248b38.jpg");
        mediaPerson2.setOccupationType(OccupationType.ACTOR);
    }

    @Test
    public void testFindAllBetween() throws DaoException, ServiceException {
        int start = 0;
        int end = 2;
        List<MediaPerson> expected = List.of(mediaPerson1, mediaPerson2);
        given(mediaPersonDao.findAllBetween(start, end)).willReturn(expected);
        List<MediaPerson> actual = mediaPersonService.findAllBetween(start, end);
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testFindById() throws DaoException, ServiceException {
        int id = 34;
        String stringId = "34";
        given(mediaPersonDao.findEntityById(id)).willReturn(mediaPerson1);
        Optional<MediaPerson> actual = mediaPersonService.findById(stringId).getKey();
        assertEquals(actual.get(), mediaPerson1);
    }

    @AfterClass
    public void clear() {
        mediaPersonService = null;
        mediaPerson1 = null;
        mediaPerson2 = null;
        mediaPersonDao = null;
        try {
            ConnectionPool.getInstance().destroyPool();
        } catch (ConnectionException e) {
            Assert.fail(e.getMessage());
        }
    }
}