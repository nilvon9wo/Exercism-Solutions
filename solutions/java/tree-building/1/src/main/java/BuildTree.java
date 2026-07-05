import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class BuildTree {

    TreeNode buildTree(ArrayList<Record> records) throws InvalidRecordsException {
        if (records.isEmpty()) {
            return null;
        }

        List<Record> sortedRecords = this.sortRecordsById(records);
        this.validateRecords(sortedRecords);
        Map<Integer, TreeNode> nodesById = this.createNodesByIdMap(sortedRecords);
        this.attachChildren(sortedRecords, nodesById);
        return nodesById.get(0);
    }

    private List<Record> sortRecordsById(final ArrayList<Record> records) {
        return records.stream()
                      .sorted(Comparator.comparing(Record::getRecordId))
                      .toList();
    }

    private Map<Integer, TreeNode> createNodesByIdMap(final List<Record> sortedRecords) {
        return sortedRecords.stream()
                            .collect(Collectors.toMap(Record::getRecordId, BuildTree::createTreeNode));
    }

    private static TreeNode createTreeNode(final Record record) {
        return new TreeNode(record.getRecordId());
    }

    private void attachChildren(final List<Record> sortedRecords, final Map<Integer, TreeNode> nodesById) {
        sortedRecords.forEach(record -> {
            if (record.getRecordId() != 0) {
                TreeNode parent = nodesById.get(record.getParentId());
                TreeNode child = nodesById.get(record.getRecordId());
                parent.getChildren().add(child);
            }
        });
    }

    private void validateRecords(List<Record> records) throws InvalidRecordsException {
        this.validateIdsAreContinuous(records);
        for (Record record : records) {
            this.validateRecord(record);
        }
    }

    private void validateIdsAreContinuous(List<Record> records) throws InvalidRecordsException {
        List<Integer> actualIds = this.extractRecordIds(records);
        List<Integer> expectedIds = this.expectedSequentialIds(records);
        if (!actualIds.equals(expectedIds)) {
            throw new InvalidRecordsException("Invalid Records");
        }
    }

    private List<Integer> extractRecordIds(final List<Record> records) {
        return records.stream()
                      .map(Record::getRecordId)
                      .toList();
    }

    private List<Integer> expectedSequentialIds(final List<Record> records) {
        return IntStream.range(0, records.size())
                        .boxed()
                        .toList();
    }

    private void validateRecord(Record record) throws InvalidRecordsException {
        if (isInvalidRecord(record)) {
            throw new InvalidRecordsException("Invalid Records");
        }
    }

    private static boolean isInvalidRecord(final Record record) {
        return isInvalidRootParent(record)
               || isSelfParentInvalid(record)
               || isParentGreaterThanChild(record);
    }

    private static boolean isInvalidRootParent(final Record record) {
        return record.getRecordId() == 0 && record.getParentId() != 0;
    }

    private static boolean isSelfParentInvalid(final Record record) {
        return record.getRecordId() != 0 && isSelfParent(record);
    }

    private static boolean isSelfParent(final Record record) {
        return record.getRecordId() == record.getParentId();
    }

    private static boolean isParentGreaterThanChild(final Record record) {
        return record.getParentId() > record.getRecordId();
    }
}