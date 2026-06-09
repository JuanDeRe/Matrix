import java.util.Objects;

public class Position {
    private Integer row;
    private Integer column;

    public Position(Integer row, Integer column) {
        setColumn(column);
        setRow(row);
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    @Override
    public boolean equals(Object object){
        if(this == object){
            return true;
        }
        if(!(object instanceof Position p)){
            return false;
        }
        return (Objects.equals(getRow(), p.getRow())) && (Objects.equals(getColumn(), p.getColumn()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

}
