import spacy
from database import Database
from translations import (
    POS_TRANSLATION, NUMBER_TRANSLATION, TENSE_TRANSLATION, CASE_TRANSLATION,
    DEGREE_TRANSLATION, DEP_TRANSLATION, PERSON_TRANSLATION, ASPECT_TRANSLATION,
    VOICE_TRANSLATION, MOOD_TRANSLATION, GENDER_TRANSLATION, DEFINITE_TRANSLATION
)

nlp = spacy.load("en_core_web_sm")


class TextProcessor:
    @staticmethod
    def process_text(text):
        doc = nlp(text)
        analyzed_words = []

        for sent in doc.sents:
            for token in sent:
                if token.is_punct or token.is_space:
                    continue

                info = {
                    "word": token.text.lower(),
                    "lemma": token.lemma_,
                    "role": TextProcessor.get_word_role(token),
                    "morphological_features": TextProcessor.get_morphological_features(token)
                }
                analyzed_words.append(info)
                Database.save_word(info)

        analyzed_words.sort(key=lambda x: x['word'])
        return analyzed_words

    @staticmethod
    def get_word_role(token):
        return DEP_TRANSLATION.get(token.dep_, "Роль не определена")

    @staticmethod
    def get_morphological_features(token):
        features = [
            f"Лексема: {token.lemma_}",
            f"Часть речи: {POS_TRANSLATION.get(token.pos_, 'Неизвестно')}"
        ]

        if token.morph.get("Number"):
            features.append(f"Число: {NUMBER_TRANSLATION.get(token.morph.get('Number')[0], 'Неизвестно')}")

        if token.morph.get("Tense"):
            features.append(f"Время: {TENSE_TRANSLATION.get(token.morph.get('Tense')[0], 'Неизвестно')}")

        if token.morph.get("Case"):
            features.append(f"Падеж: {CASE_TRANSLATION.get(token.morph.get('Case')[0], 'Неизвестно')}")

        if token.morph.get("Degree"):
            features.append(f"Степень сравнения: {DEGREE_TRANSLATION.get(token.morph.get('Degree')[0], 'Неизвестно')}")

        if token.morph.get("Person"):
            features.append(f"Лицо: {PERSON_TRANSLATION.get(token.morph.get('Person')[0], 'Неизвестно')}")

        if token.morph.get("Aspect"):
            features.append(f"Вид: {ASPECT_TRANSLATION.get(token.morph.get('Aspect')[0], 'Неизвестно')}")

        if token.morph.get("Voice"):
            features.append(f"Залог: {VOICE_TRANSLATION.get(token.morph.get('Voice')[0], 'Неизвестно')}")

        if token.morph.get("Mood"):
            features.append(f"Наклонение: {MOOD_TRANSLATION.get(token.morph.get('Mood')[0], 'Неизвестно')}")

        if token.morph.get("Gender"):
            features.append(f"Род: {GENDER_TRANSLATION.get(token.morph.get('Gender')[0], 'Неизвестно')}")

        if token.morph.get("Definite"):
            features.append(f"Определённость: {DEFINITE_TRANSLATION.get(token.morph.get('Definite')[0], 'Неизвестно')}")

        if token.morph.get("Poss"):
            features.append(f"Притяжательная форма: {'Да' if token.morph.get('Poss')[0] == 'Yes' else 'Нет'}")

        return ", ".join(features) if features else "Нет данных"