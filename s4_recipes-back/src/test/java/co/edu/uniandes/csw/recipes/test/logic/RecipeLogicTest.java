/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.recipes.test.logic;

import co.edu.uniandes.csw.recipes.ejb.RecipeLogic;
import co.edu.uniandes.csw.recipes.entities.RecipeEntity;
import co.edu.uniandes.csw.recipes.persistence.RecipePersistence;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.runner.RunWith;

/**
 * me tocó crearlo todo
 * @author estudiante
 */
@RunWith(Arquillian.class)
public class RecipeLogicTest {

    private PodamFactory factory = new PodamFactoryImpl();

    @Inject
    private RecipeLogic recipeLogic;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    private List<RecipeEntity> data = new ArrayList<RecipeEntity>();

    /**
     * @return Devuelve el jar que Arquillian va a desplegar en Payara embebido.
     * El jar contiene las clases, el descriptor de la base de datos y el
     * archivo beans.xml para resolver la inyección de dependencias.
     */
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(RecipeEntity.class.getPackage())
                .addPackage(RecipeLogic.class.getPackage())
                .addPackage(RecipePersistence.class.getPackage())
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsManifestResource("META-INF/beans.xml", "beans.xml");
    }

    /**
     * Configuración inicial de la prueba.
     */
    @Before
    public void configTest() {
        try {
            utx.begin();
            clearData();
            insertData();
            utx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                utx.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * Limpia las tablas que están implicadas en la prueba.
     */
    private void clearData() {
        em.createQuery("delete from RecipeEntity").executeUpdate();

    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las
     * pruebas.
     */
    private void insertData() {
        for (int i = 0; i < 3; i++) {
            Entity books = factory.manufacturePojo(BookEntity.class);
            em.persist(books);
            booksData.add(books);
        }
        for (int i = 0; i < 3; i++) {
            EditorialEntity entity = factory.manufacturePojo(EditorialEntity.class);
            em.persist(entity);
            data.add(entity);
            if (i == 0) {
                booksData.get(i).setEditorial(entity);
            }
        }
    }

    /**
     * Prueba para crear un Editorial.
     *
     * @throws co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException
     */
    @Test
    public void createEditorialTest() throws BusinessLogicException {
        EditorialEntity newEntity = factory.manufacturePojo(EditorialEntity.class);
        EditorialEntity result = editorialLogic.createEditorial(newEntity);
        Assert.assertNotNull(result);
        EditorialEntity entity = em.find(EditorialEntity.class, result.getId());
        Assert.assertEquals(newEntity.getId(), entity.getId());
        Assert.assertEquals(newEntity.getName(), entity.getName());
    }
}
