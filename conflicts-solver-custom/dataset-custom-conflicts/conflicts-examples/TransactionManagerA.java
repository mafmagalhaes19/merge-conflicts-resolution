public class TransactionManager {
    public void executeTransaction() {
        logTransaction();
        processTransaction();
    }

    private void logTransaction() {
        System.out.println("Transaction logged.");
    }

    private void processTransaction() {
        System.out.println("Transaction processed.");
    }
}

