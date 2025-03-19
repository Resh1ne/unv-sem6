import tkinter as tk
from tkinter import filedialog, scrolledtext, messagebox, ttk
import time
from text_processor import TextProcessor
from database import Database
from ttkthemes import ThemedTk


def open_file(text_area):
    file_path = filedialog.askopenfilename(filetypes=[("Text files", "*.txt")])
    if file_path:
        try:
            with open(file_path, 'r', encoding='utf-8') as file:
                content = file.read()

            start_time = time.time()
            analyzed_words = TextProcessor.process_text(content)
            end_time = time.time()

            processing_time = end_time - start_time

            result = format_result_with_styles(analyzed_words)
            result += f"\nВремя обработки текста: {processing_time:.4f} секунд\n"

            text_area.delete(1.0, tk.END)
            text_area.insert(tk.END, result)
        except Exception as e:
            messagebox.showerror("Ошибка", f"Не удалось обработать файл: {e}")


def format_result_with_styles(analyzed_words):
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
            f"Лексема: {word[2]}\n"
            f"Роль в предложении: {word[3]}\n"
            f"Морфологические признаки: {word[4]}\n"
            f"{'-' * 50}\n"
        )
    text_area.delete(1.0, tk.END)
    text_area.insert(tk.END, result)


def show_help():
    help_window = tk.Toplevel()
    help_window.title("Помощь")
    help_window.geometry("700x500")

    help_text = """
    Возможности приложения:

    1. Открытие текстового файла:
       - Приложение позволяет открывать текстовые файлы (.txt) для анализа.
       - После открытия файла текст анализируется, и каждое слово разбирается на:
         - Лемму (нормальную форму слова).
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

    5. Время обработки:
       - Приложение показывает время, затраченное на обработку текста.

    6. Поиск слова:
       - Вы можете найти слово в базе данных, используя поле ввода и кнопку "Найти".
       
    7. Редактирование:
       - Для редактирования записи, выделите её, а затей нажмите кнопку "Редактировать".
    """

    help_label = tk.Label(help_window, text=help_text, justify=tk.LEFT, padx=10, pady=10, font=("Arial", 12))
    help_label.pack(fill=tk.BOTH, expand=True)


def perform_search(search_entry, text_area):
    substring_to_search = search_entry.get().strip().lower()
    if not substring_to_search:
        messagebox.showwarning("Ошибка", "Введите часть слова для поиска.")
        return

    found_words = Database.search_words_by_substring(substring_to_search)

    if not found_words:
        text_area.delete(1.0, tk.END)
        text_area.insert(tk.END, f"Слова, содержащие '{substring_to_search}', не найдены.\n")
    else:
        result = f"Результаты поиска для '{substring_to_search}':\n\n"
        for word in found_words:
            result += (
                f"ID: {word[0]}\n"
                f"Слово: {word[1]}\n"
                f"Роль: {word[2]}\n"
                f"Признаки: {word[3]}\n"
                f"{'-' * 50}\n"
            )
        text_area.delete(1.0, tk.END)
        text_area.insert(tk.END, result)


def edit_word(text_area):
    try:
        selected_text = text_area.get(tk.SEL_FIRST, tk.SEL_LAST)
    except tk.TclError:
        messagebox.showwarning("Ошибка", "Выделите запись для редактирования.")
        return

    try:
        word_id = int(selected_text.split("\n")[0].split(": ")[1])
    except (IndexError, ValueError):
        messagebox.showwarning("Ошибка", "Не удалось извлечь ID записи.")
        return

    words = Database.get_all_words()
    word_info = next((word for word in words if word[0] == word_id), None)
    if not word_info:
        messagebox.showwarning("Ошибка", "Запись не найдена.")
        return

    edit_window = tk.Toplevel()
    edit_window.title("Редактирование записи")
    edit_window.geometry("500x400")

    tk.Label(edit_window, text="Слово:").pack(pady=5)
    word_entry = ttk.Entry(edit_window, width=40, font=("Arial", 12))
    word_entry.pack(pady=5)
    word_entry.insert(0, word_info[1])

    tk.Label(edit_window, text="Лемма:").pack(pady=5)
    lemma_entry = ttk.Entry(edit_window, width=40, font=("Arial", 12))
    lemma_entry.pack(pady=5)
    lemma_entry.insert(0, word_info[2])

    tk.Label(edit_window, text="Роль:").pack(pady=5)
    role_entry = ttk.Entry(edit_window, width=40, font=("Arial", 12))
    role_entry.pack(pady=5)
    role_entry.insert(0, word_info[3])

    tk.Label(edit_window, text="Морфологические признаки:").pack(pady=5)
    features_entry = ttk.Entry(edit_window, width=40, font=("Arial", 12))
    features_entry.pack(pady=5)
    features_entry.insert(0, word_info[4])

    def save_edited_word():
        updated_info = {
            "word": word_entry.get(),
            "lemma": lemma_entry.get(),
            "role": role_entry.get(),
            "morphological_features": features_entry.get()
        }
        Database.update_word(word_id, updated_info)
        messagebox.showinfo("Успех", "Запись успешно обновлена.")
        edit_window.destroy()
        view_database(text_area)  # Обновляем отображение базы данных

    save_button = ttk.Button(edit_window, text="Сохранить", command=save_edited_word)
    save_button.pack(pady=10)


def create_ui():
    root = ThemedTk(theme="arc")
    root.title("Анализатор текста")
    root.geometry("800x600")

    style = ttk.Style()
    style.configure("TButton", font=("Arial", 12), padding=10)
    style.configure("TLabel", font=("Arial", 12))
    style.configure("TEntry", font=("Arial", 12))

    toolbar = ttk.Frame(root)
    toolbar.pack(side=tk.TOP, fill=tk.X)

    open_button = ttk.Button(toolbar, text="Открыть файл", command=lambda: open_file(text_area))
    open_button.pack(side=tk.LEFT, padx=5, pady=5)

    view_db_button = ttk.Button(toolbar, text="Просмотреть базу данных", command=lambda: view_database(text_area))
    view_db_button.pack(side=tk.LEFT, padx=5, pady=5)

    edit_button = ttk.Button(toolbar, text="Редактировать", command=lambda: edit_word(text_area))
    edit_button.pack(side=tk.LEFT, padx=5, pady=5)

    help_button = ttk.Button(root, text="Помощь", command=show_help)
    help_button.place(relx=0.95, rely=0.02, anchor=tk.NE)

    text_area = scrolledtext.ScrolledText(root, wrap=tk.WORD, width=90, height=25, font=("Arial", 12))
    text_area.pack(padx=10, pady=10, fill=tk.BOTH, expand=True)

    text_area.tag_configure("header", font=("Arial", 14, "bold"), foreground="blue")
    text_area.tag_configure("word", font=("Arial", 12, "bold"), foreground="green")
    text_area.tag_configure("role", font=("Arial", 12), foreground="purple")
    text_area.tag_configure("features", font=("Arial", 12), foreground="black")

    search_frame = ttk.Frame(root)
    search_frame.pack(side=tk.BOTTOM, fill=tk.X, padx=10, pady=10)

    search_entry = ttk.Entry(search_frame, width=40, font=("Arial", 12))
    search_entry.pack(side=tk.LEFT, padx=5, pady=5, fill=tk.X, expand=True)

    search_button = ttk.Button(search_frame, text="Найти", command=lambda: perform_search(search_entry, text_area))
    search_button.pack(side=tk.RIGHT, padx=5, pady=5)

    return root


if __name__ == "__main__":
    root = create_ui()
    root.mainloop()
