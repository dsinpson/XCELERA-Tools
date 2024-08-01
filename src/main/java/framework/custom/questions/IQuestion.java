package framework.custom.questions;

import java.sql.SQLException;

public interface IQuestion <ANSWER> {
    ANSWER ask() throws SQLException;
}
