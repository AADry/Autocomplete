Проект Autocomplete — тестовое задание на стажировку java-разработчиком. Приложение представляет из себя поиск строк по одному из столбцов, при помощи префикса.
#Принцип работы
  При запуске приложения составляется по одному из столбцов составляется префиксное дерево (англ. trie). Вводимая пользователем строка ищется в дереве, при успешном поиске получая на выходе список индексов. По полученным индексам нужные строки достаются из файла, сортируются и выводятся на консоль после заголовка, по которому происходила сортировка.
