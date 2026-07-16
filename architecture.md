```text
                  +--------------------+
                  |   QueueCTL CLI     |
                  +---------+----------+
                            |
                            |
                    Picocli Commands
                            |
        +-------------------+------------------+
        |                   |                  |
        |                   |                  |
    Enqueue             Worker             Status/List
        |                   |                  |
        +-------------------+------------------+
                            |
                     JobRepository
                            |
                     SQLite Database
                            |
          +-----------------+----------------+
          |                                  |
      Pending Jobs                    Dead Letter Queue
          |                                  |
          +----------- WorkerRunner ---------+
                          |
                     Command Executor
                          |
                System Command Execution
```