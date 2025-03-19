import tkinter as tk
from tkinter import filedialog, scrolledtext, messagebox
from text_processor import TextProcessor
from database import Database


def open_file(text_area):
    file_path = filedialog.askopenfilename(filetypes=[("Text files", "*.txt")])
    if file_path:
        try:
            with open(file_path, 'r', encoding='utf-8') as file:
                content = file.read()
            analyzed_words = TextProcessor.process_text(content)
            result = format_result(analyzed_words)
            text_area.delete(1.0, tk.END)
            text_area.insert(tk.END, result)
        except Exception as e:
            messagebox.showerror("Ошибка", f"Не удалось обработать файл: {e}")


def format_result(analyzed_words):
    result = "Список слов с информацией:\n\n"
    for info in analyzed_words:
        result += (
            f"🔹 Слово: {info['word']}\n"
            f"   Роль в предложении: {info['role']}\n"
            f"   Морфологические признаки:\n"
            f"      {info['morphological_features'].replace(', ', '\n      ')}\n"
            f"{'-' * 50}\n"
        )
    return result


def view_database(text_area):
    words = Database.get_all_words()
    result = "Содержимое базы данных:\n\n"
    for word in words:
        result += (
            f"ID: {word[0]}\n"
            f"Слово: {word[1]}\n"
            f"Роль: {word[2]}\n"
            f"Признаки: {word[3]}\n"
            f"{'-' * 50}\n"
        )
    text_area.delete(1.0, tk.END)
    text_area.insert(tk.END, result)


def show_help():
    help_window = tk.Toplevel()
    help_window.title("Помощь")
    help_window.geometry("600x400")

    help_text = """
    Возможности приложения:

    1. Открытие текстового файла:
       - Приложение позволяет открывать текстовые файлы (.txt) для анализа.
       - После открытия файла текст анализируется, и каждое слово разбирается на:
         - Роль в предложении (например, подлежащее, сказуемое).
         - Морфологические признаки (часть речи, число, время и т.д.).

    2. Сохранение данных:
       - Результаты анализа сохраняются в базу данных SQLite.
       - Вы можете просмотреть сохраненные данные с помощью кнопки "Просмотреть базу данных".

    3. Просмотр базы данных:
       - В базе данных хранятся все проанализированные слова с их признаками.
       - Вы можете просмотреть содержимое базы данных в текстовом поле.

    4. Сортировка:
       - Слова в результатах анализа сортируются по алфавиту.

    Для начала работы нажмите кнопку "Открыть файл" и выберите текстовый файл.
    """

    help_label = tk.Label(help_window, text=help_text, justify=tk.LEFT, padx=10, pady=10)
    help_label.pack(fill=tk.BOTH, expand=True)


def create_ui():
    root = tk.Tk()
    root.title("Анализатор текста")
    root.geometry("800x600")

    toolbar = tk.Frame(root)
    toolbar.pack(side=tk.TOP, fill=tk.X)

    open_button = tk.Button(toolbar, text="Открыть файл", command=lambda: open_file(text_area))
    open_button.pack(side=tk.LEFT, padx=5, pady=5)

    view_db_button = tk.Button(toolbar, text="Просмотреть базу данных", command=lambda: view_database(text_area))
    view_db_button.pack(side=tk.LEFT, padx=5, pady=5)

    help_button = tk.Button(root, text="Помощь", command=show_help)
    help_button.place(relx=0.95, rely=0.02, anchor=tk.NE)

    text_area = scrolledtext.ScrolledText(root, wrap=tk.WORD, width=90, height=30)
    text_area.pack(padx=10, pady=10, fill=tk.BOTH, expand=True)

    return root


if __name__ == "__main__":
    root = create_ui()
    root.mainloop()
