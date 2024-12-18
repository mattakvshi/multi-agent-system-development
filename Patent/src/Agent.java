import java.util.*;
import java.util.stream.Collectors;

public class Agent {

    private static final String TARGET_STRING = "abcdefghijklmnopqrstuvwxyz";

    private static final int LENGTH_TARGET_TASK = 5;

    private int id;
    private List<String> targetTask;
    private List<String> importantPatents;
    private List<String> uselessPatents;
    private int numberOfIteration;
    private int numberOfCommunications;

    public Agent(int id) {
        this.id = id;
        Random random = new Random();

        //self.target_task = sorted([target_str[rnd.randint(0, len(target_str) - 1)] for i in range(len_target_task)])   # создаём случайный целевой список
        //Тоже самое но на джава:
        this.targetTask = random.ints(LENGTH_TARGET_TASK, 0, TARGET_STRING.length())
                .mapToObj(i -> String.valueOf(TARGET_STRING.charAt(i)))
                .sorted()
                .collect(Collectors.toList());

        this.importantPatents = new ArrayList<>();
        this.uselessPatents = new ArrayList<>();
        this.numberOfIteration = 0;
        this.numberOfCommunications = 0;
    }


    public void getInfo() {
        Map<String, Long> missingImportantPatents = checkingTask();

        //for key, value in missing_important_patents.items():   # перебор ключей и значений словаря недостающих патентов
        //mis_imp_pat_list.extend(key * value)               # заполнение списка недостающих патентов
        //mis_imp_pat_list = sorted(mis_imp_pat_list)            # сортировка списка недостающих патентов
        //Тоже самое но на джава:
        List<String> misImpPatList = missingImportantPatents.entrySet().stream()
                .flatMap(entry -> Collections.nCopies(entry.getValue().intValue(), entry.getKey()).stream())
                .sorted()
                .collect(Collectors.toList());

        System.out.println("Агент: id = " + id + "; ЦЕЛЕВОЙ НАБОР ПАТЕНТОВ: " + targetTask
                + "; УЖЕ СОБРАЛ: " + importantPatents
                + "; ЕЩЁ НУЖНЫ: " + uselessPatents
                + ", ГОТОВ ОТДАТЬ: " + misImpPatList);
    }


    public void obtainingAPatent(String patent) {

        long countPatent = targetTask.stream().filter(p -> p.equals(patent)).count();

        if (countPatent > 0 && importantPatents.stream().filter(p -> p.equals(patent)).count() < countPatent) {
            importantPatents.add(patent);
            Collections.sort(importantPatents);
        } else {
            uselessPatents.add(patent);
            Collections.sort(uselessPatents);
        }

    }


    public void swap(String patent) {
        this.numberOfCommunications++;
        this.obtainingAPatent(patent);
    }

    public void updateState() {
        this.numberOfIteration++;
    }


    public Map<String, Long> checkingTask() {

        Map<String, Long> targetPatentCount = targetTask.stream()
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()));

        Map<String, Long> collectedPatentCount = importantPatents.stream()
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()));

        Map<String, Long> missingPatents = new HashMap<>();

        for (Map.Entry<String, Long> entry : targetPatentCount.entrySet()) {

            long collectedCount = collectedPatentCount.getOrDefault(entry.getKey(), 0L);

            if (collectedCount < entry.getValue()) {
                missingPatents.put(entry.getKey(), entry.getValue() - collectedCount);
            }
        }
        return missingPatents;
    }


    public boolean checkCompletion() {
        return checkingTask().isEmpty();
    }

    public void winInfo() {
        System.out.println("Агенту " + id + " == " + targetTask
                + " потребовалось " + numberOfIteration + " ИТЕРАЦИЙ и "
                + numberOfCommunications + " ОБМЕНОВ");
    }

    public int getId() {
        return id;
    }

    public List<String> getTargetTask() {
        return targetTask;
    }

    public List<String> getImportantPatents() {
        return importantPatents;
    }

    public List<String> getUselessPatents() {
        return uselessPatents;
    }

    public int getNumberOfIteration() {
        return numberOfIteration;
    }

    public int getNumberOfCommunications() {
        return numberOfCommunications;
    }
}
