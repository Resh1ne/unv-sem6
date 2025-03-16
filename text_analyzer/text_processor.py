import string
import nltk
from nltk import pos_tag
from nltk.tokenize import word_tokenize

nltk.download('punkt')
nltk.download('punkt_tab')
nltk.download('averaged_perceptron_tagger')
nltk.download('averaged_perceptron_tagger_eng')

punctuation = set(string.punctuation)


def process_text(text):
    words = word_tokenize(text)

    filtered_words = [word.lower() for word in words if word not in punctuation]

    tagged_words = pos_tag(filtered_words)

    analyzed_words = []
    for word, pos in tagged_words:
        info = {
            "word": word,
            "part_of_speech": pos,
            "role": get_word_role(pos),
        }
        analyzed_words.append(info)

    analyzed_words.sort(key=lambda x: x['word'])

    return analyzed_words


def get_word_role(pos):
    # Обработка глаголов (VB)
    if pos.startswith('VB'):
        if pos == 'VBD':  # Прошедшее время
            return "Может быть сказуемым (прошедшее время)"
        elif pos == 'VBG':  # Герундий или причастие настоящего времени
            return "Может быть частью сказуемого или определением (герундий/причастие)"
        elif pos == 'VBN':  # Причастие прошедшего времени
            return "Может быть частью сказуемого или определением (причастие прошедшего времени)"
        elif pos == 'VBP':  # Настоящее время (не 3-е лицо единственное число)
            return "Может быть сказуемым (настоящее время)"
        elif pos == 'VBZ':  # Настоящее время (3-е лицо единственное число)
            return "Может быть сказуемым (настоящее время, 3-е лицо)"
        else:  # Базовая форма (инфинитив)
            return "Может быть сказуемым (инфинитив)"

    # Обработка прилагательных
    elif pos.startswith('JJ'):
        if pos == 'JJR':  # Сравнительная степень
            return "Может быть определением (сравнительная степень)"
        elif pos == 'JJS':  # Превосходная степень
            return "Может быть определением (превосходная степень)"
        else:  # Обычное прилагательное
            return "Может быть определением"

    # Обработка наречий)
    elif pos.startswith('RB'):
        if pos == 'RBR':  # Сравнительная степень
            return "Может быть обстоятельством (сравнительная степень)"
        elif pos == 'RBS':  # Превосходная степень
            return "Может быть обстоятельством (превосходная степень)"
        else:  # Обычное наречие
            return "Может быть обстоятельством"

    # Обработка местоимений
    elif pos.startswith('PRP'):
        if pos == 'PRP$':  # Притяжательное местоимение
            return "Может быть определением (притяжательное)"
        else:  # Личное местоимение
            return "Может быть подлежащим или дополнением"

    elif pos.startswith('NN'):  # Сущесвтиетльное
        return "Может быть подлежащим или дополнением"
    elif pos.startswith('IN'):  # Предлоги и подчинительные союзы
        return "Может быть частью обстоятельства или связующим элементом"
    elif pos.startswith('CC'):  # Сочинительные союзы
        return "Связывает слова или предложения"
    elif pos.startswith('CD'):  # Числительные
        return "Может быть определением или частью дополнения"
    elif pos.startswith('MD'):  # Модальные глаголы
        return "Может быть частью сказуемого"
    elif pos.startswith('TO'):  # Частица to
        return "Может быть частью инфинитива"
    elif pos.startswith('WRB'):  # Вопросительные наречия
        return "Может быть частью обстоятельства"
    elif pos.startswith('POS'):  # Притяжательное окончание
        return "Обозначает принадлежность"
    elif pos.startswith('RP'):  # Частицы
        return "Может быть частью фразового глагола"
    elif pos.startswith('FW'):  # Иностранные слова
        return "Иностранное слово, роль зависит от контекста"
    elif pos.startswith('UH'):  # Междометия
        return "Выражает эмоцию или реакцию"
    else:
        return "Роль не определена"
