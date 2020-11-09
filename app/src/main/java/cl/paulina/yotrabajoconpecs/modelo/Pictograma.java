package cl.paulina.yotrabajoconpecs.modelo;

public class Pictograma {
    public int _id;
    public String keyword;

    public Pictograma(int _id, String keyword) {
        this._id = _id;
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setUrl(String url) {
        this.keyword = url;
    }

    public int getId() {

        return _id;
    }

    public void setId(int id) {

        this._id = id;
    }
}
