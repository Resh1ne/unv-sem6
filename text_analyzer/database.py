import sqlite3

DATABASE_NAME = "text_analysis.db"


class Database:
    @staticmethod
    def initialize():
        with sqlite3.connect(DATABASE_NAME) as conn:
            cursor = conn.cursor()
            cursor.execute('''
                CREATE TABLE IF NOT EXISTS words (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    word TEXT NOT NULL,
                    lemma TEXT NOT NULL,
                    role TEXT NOT NULL,
                    morphological_features TEXT NOT NULL
                )
            ''')
            conn.commit()

    @staticmethod
    def save_word(word_info):
        with sqlite3.connect(DATABASE_NAME) as conn:
            cursor = conn.cursor()
            cursor.execute('''
                INSERT INTO words (word, lemma, role, morphological_features)
                VALUES (?, ?, ?, ?)
            ''', (
                word_info["word"],
                word_info["lemma"],
                word_info["role"],
                word_info["morphological_features"]
            ))
            conn.commit()

    @staticmethod
    def get_all_words():
        with sqlite3.connect(DATABASE_NAME) as conn:
            cursor = conn.cursor()
            cursor.execute("SELECT * FROM words")
            return cursor.fetchall()

    @staticmethod
    def search_words_by_substring(substring):
        with sqlite3.connect(DATABASE_NAME) as conn:
            cursor = conn.cursor()
            cursor.execute('''
                SELECT * FROM words
                WHERE word LIKE ?
            ''', (f"%{substring}%",))
            return cursor.fetchall()

    @staticmethod
    def update_word(word_id, word_info):
        with sqlite3.connect(DATABASE_NAME) as conn:
            cursor = conn.cursor()
            cursor.execute('''
                UPDATE words
                SET word = ?, lemma = ?, role = ?, morphological_features = ?
                WHERE id = ?
            ''', (
                word_info["word"],
                word_info["lemma"],
                word_info["role"],
                word_info["morphological_features"],
                word_id
            ))
            conn.commit()


Database.initialize()