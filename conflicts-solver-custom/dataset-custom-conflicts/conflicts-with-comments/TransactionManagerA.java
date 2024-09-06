public class TransactionManager {
    // We should process a transaction before logging it
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

