package dataProvider;

import utils.dbUtil.HibernateUtil;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.opentest4j.TestSkippedException;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;


public class DBDataSource implements ArgumentsProvider, AnnotationConsumer<DataFromDB>  {
    Logger logger = Logger.getGlobal();
    String table;
    String[] columns;
    List<Object[]> dbResults;

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        dbResults = getValuesFromDB(table, columns);
        if(dbResults != null) {
            return getStreamOfArguments(dbResults);
        } else {
            String message = String.format("Test %s#%s was skipped due to invalid arguments",
                    extensionContext.getRequiredTestClass().getName(), extensionContext.getRequiredTestMethod().getName());
            throw new TestSkippedException(message);
        }
    }

    private List<Object[]> getValuesFromDB(String table, String[] columns){
        try {
            String query = String.format("SELECT %s FROM %s", String.join(", ", columns), table);
            return HibernateUtil.runSelectQuery(query);
        } catch (PersistenceException ex) {
            logger.log(Level.WARNING, "Error when executing the SQL query. Invalid request parameters may be specified.");
            ex.printStackTrace();
            return null;
        }
    }

    private Stream<Arguments> getStreamOfArguments(List<Object[]> args){
        return args.stream().map(Arguments::of);
    }

    @Override
    public void accept(DataFromDB dataFromDB) {
        table = dataFromDB.table();
        columns = dataFromDB.columns();
    }
}
