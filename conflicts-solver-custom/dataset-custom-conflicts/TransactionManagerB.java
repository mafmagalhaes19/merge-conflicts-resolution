public class TransactionManager {
    public void executeTransaction() {
        processTransaction();
        logTransaction();
    }

    private void logTransaction() {
        System.out.println("Transaction logged.");
    }

    private void processTransaction() {
        System.out.println("Transaction processed.");
    }
}
