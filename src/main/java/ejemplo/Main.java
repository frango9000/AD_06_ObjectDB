/*

Bases de datos orientadas a objecto

permiten almacenar e recuperar obxectos de forma nativa  , e decir a unidade de información a ser almacenada e o propio obxecto

imos traballar co xestor de bases de datos denominado Objectdb  en modo embebido, e dicer non traballaremos en modo cliente servidor senon que se creara una base de datos dentro do propio proxecto no momento de invocala .

a base a instalar e objectdb-2.7.5_01 e a tedes na vosa carpeta local (/home/oracle )



tedes un manual que indica como crear una base de datos de proba denominada “tutorial”.
seguide os pasos indicados desenvolvendo as seguintes accions:

cargar nas librerias do proxecto a libreria objectdb.jar


conectarse a base
insercion de obxectos,
listado de todos os obxectos creados
modificacion dun obxecto
modificacion masiva de varios obxectos
eliminacion dun obxecto



 */
package ejemplo;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class Main {

    public static EntityManager em;

    public static void main(String[] args) {
        //conectarse a base
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("src/main/java/ejemplo/futbol.odb");
        em = emf.createEntityManager();

        // insercion de obxectos,
        em.getTransaction().begin();
        for (int i = 0; i < 25; i++) {
            Jugador jugador = new Jugador(('A' + i) + "", i);
            em.persist(jugador);
        }
        em.getTransaction().commit();

        Query q1 = em.createQuery("SELECT COUNT(j) FROM Jugador j");
        System.out.println("Total Points: " + q1.getSingleResult());

        // listado de todos os obxectos creados
        queryPrintAll();

        Jugador j5 = buscarDorsal(5);
        System.out.println(j5.toString());

        //modificacion dun obxecto
        em.getTransaction().begin();
        j5.setNombre("Jugador 5");
        em.getTransaction().commit();

        j5 = buscarDorsal(5);
        System.out.println(j5);

        // modificacion masiva de varios obxectos
        anularDorsalesMayoresA(20);
        queryPrintAll();

        // eliminacion dun obxecto
        Jugador j4 = buscarDorsal(4);
        em.getTransaction().begin();
        em.remove(j4);
        em.getTransaction().commit();
        queryPrintAll();

        // Close the database connection:
        em.close();
        emf.close();
    }

    private static void queryPrintAll() {
        TypedQuery<Jugador> query = em.createQuery("SELECT j FROM Jugador j", Jugador.class);
        List<Jugador> results = query.getResultList();
        for (Jugador jugador : results) {
            System.out.println(jugador);
        }
        System.out.println();
    }

    private static Jugador buscarDorsal(int dorsalQuery) {
        TypedQuery<Jugador> queryOne = em.createQuery("SELECT j FROM Jugador j where j.dorsal = :dorsal", Jugador.class);
        queryOne.setParameter("dorsal", dorsalQuery);
        return queryOne.getSingleResult();
    }

    private static void anularDorsalesMayoresA(int mayorA) {
        em.getTransaction().begin();
        TypedQuery<Jugador> query = em.createQuery("SELECT j FROM Jugador j where j.dorsal >= :dorsal", Jugador.class);
        query.setParameter("dorsal", mayorA);
        List<Jugador> results = query.getResultList();
        for (Jugador jugador : results) {
            jugador.setDorsal(-1);
        }
        em.getTransaction().commit();
    }
}
