class Extractor {
    private int street;
    private int avenue;
    private boolean autorizar;

    public Extractor(int street, int avenue, boolean autorizar) {
        this.street = street;
        this.avenue = avenue;
        this.autorizar = autorizar;
    }

    public int getStreet() {
        return street;
    }

    public int getAvenue() {
        return avenue;
    }

    public boolean getAutorizacion() {
        return autorizar;
    }

    public void updateAutorizacion() {
        autorizar = !autorizar;
    }

    public void incrementStreet() {
        street++;
    }

    public void incrementAvenue() {
        avenue++;
    }

    public void decreaseStreet() {
        street--;
    }

    public void decreaseAvenue() {
        avenue--;
    }
}