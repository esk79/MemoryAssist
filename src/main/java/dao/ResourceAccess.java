package dao;

import com.google.inject.Inject;
import models.Resource;
import utils.ConnectionProvider;
import utils.Statements;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Created by EvanKing on 8/2/17.
 */
public class ResourceAccess extends AbstractAccess {

    @Inject
    ResourceAccess(ConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    public Optional<Resource> getResource(int resourceID) throws SQLException {
        return select(Statements.getResource(resourceID), optional(this::getResource));
    }

    public void insertResource(Resource resource) throws SQLException {
        update(Statements.insertResource(resource.getTitle(), resource.getMarkdown()), 1);
    }

    private Resource getResource(ResultSet resultSet) throws SQLException {

        String title = resultSet.getString("title");
        String markdown = resultSet.getString("markdown");

        return new Resource(title, markdown);
    }
}
