import tkinter as tk
from tkinter import filedialog, scrolledtext, messagebox
from text_processor import process_text


def open_file(text_area):
    file_path = filedialog.askopenfilename(filetypes=[("Text files", "*.txt")])
    if file_path:
        try:
            with open(file_path, 'r', encoding='utf-8') as file:
                content = file.read()
            analyzed_words = process_text(content)
            result = format_result(analyzed_words)
            text_area.delete(1.0, tk.END)
            text_area.insert(tk.END, result)
        except Exception as e:
            messagebox.showerror("Ошибка", f"Не удалось обработать файл: {e}")


def format_result(analyzed_words):
    result = "Список слов с информацией:\n\n"
    for info in analyzed_words:
        result += (
            f"Слово: {info['word']}\n"
            f"Часть речи: {info['part_of_speech']}\n"
            f"Роль в предложении: {info['role']}\n"
            f"{'-' * 30}\n"
        )
    return result


def create_ui():
    root = tk.Tk()
    root.title("Анализатор текста")
    root.geometry("800x600")

    open_button = tk.Button(root, text="Открыть файл", command=lambda: open_file(text_area))
    open_button.pack(pady=10)

    text_area = scrolledtext.ScrolledText(root, wrap=tk.WORD, width=90, height=30)
    text_area.pack(padx=10, pady=10)

    return root
