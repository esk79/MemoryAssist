package dao;

import com.google.inject.Inject;
import models.Resource;
import utils.ConnectionProvider;
import utils.Statements;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Created by EvanKing on 8/2/17.
 */
public class ResourceAccess extends AbstractAccess {

    @Inject
    ResourceAccess(ConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    public Optional<Resource> getResource(String uid) throws SQLException {
        return select(Statements.getResource(uid), optional(this::getResource));
    }

    public List<Resource> getAllResources() throws SQLException {
        return select(conn -> conn.prepareStatement(Statements.GET_ALL_RESOURCES),
                list(this::getResource));
    }

    public void insertResource(Resource resource) throws SQLException {
        update(Statements.insertResource(resource.getUid(), resource.getTitle(), resource.getMarkdown()), 1);
    }

    public void updateResource(Resource resource) throws SQLException {
        update(Statements.updateResource(resource.getUid(), resource.getTitle(), resource.getMarkdown()), 1);
    }

    public void deleteResource(String uid) throws SQLException {
        update(Statements.deleteResource(uid), 1);
    }

    private Resource getResource(ResultSet resultSet) throws SQLException {

        String title = resultSet.getString("title");
        String markdown = resultSet.getString("markdown");

        return new Resource(title, markdown);
    }
}
