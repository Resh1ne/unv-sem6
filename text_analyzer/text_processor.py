import string
import nltk
from nltk import pos_tag
from nltk.tokenize import word_tokenize

nltk.download('punkt')
nltk.download('punkt_tab')
nltk.download('averaged_perceptron_tagger')
nltk.download('averaged_perceptron_tagger_eng')

punctuation = set(string.punctuation)


class TextProcessor:
    @staticmethod
    def process_text(text):
        words = word_tokenize(text)
        filtered_words = [word.lower() for word in words if word not in punctuation]
        tagged_words = pos_tag(filtered_words)

        analyzed_words = []
        for word, pos in tagged_words:
            info = {
                "word": word,
                "part_of_speech": pos,
                "role": TextProcessor.get_word_role(pos),
                # "morphological_features": TextProcessor.get_morphological_features(word),
            }
            analyzed_words.append(info)

        analyzed_words.sort(key=lambda x: x['word'])
        return analyzed_words

    @staticmethod
    def get_word_role(pos):
        if pos.startswith('VB'):
            if pos == 'VBD':
                return "Может быть сказуемым (прошедшее время)"
            elif pos == 'VBG':
                return "Может быть частью сказуемого или определением (герундий/причастие)"
            elif pos == 'VBN':
                return "Может быть частью сказуемого или определением (причастие прошедшего времени)"
            elif pos == 'VBP':
                return "Может быть сказуемым (настоящее время)"
            elif pos == 'VBZ':
                return "Может быть сказуемым (настоящее время, 3-е лицо)"
            else:
                return "Может быть сказуемым (инфинитив)"

        elif pos.startswith('JJ'):
            if pos == 'JJR':
                return "Может быть определением (сравнительная степень)"
            elif pos == 'JJS':
                return "Может быть определением (превосходная степень)"
            else:
                return "Может быть определением"

        elif pos.startswith('RB'):
            if pos == 'RBR':
                return "Может быть обстоятельством (сравнительная степень)"
            elif pos == 'RBS':
                return "Может быть обстоятельством (превосходная степень)"
            else:
                return "Может быть обстоятельством"

        elif pos.startswith('PRP'):
            if pos == 'PRP$':
                return "Может быть определением (притяжательное)"
            else:
                return "Может быть подлежащим или дополнением"

        elif pos.startswith('NN'):
            return "Может быть подлежащим или дополнением"
        elif pos.startswith('IN'):
            return "Может быть частью обстоятельства или связующим элементом"
        elif pos.startswith('CC'):
            return "Связывает слова или предложения"
        elif pos.startswith('CD'):
            return "Может быть определением или частью дополнения"
        elif pos.startswith('MD'):
            return "Может быть частью сказуемого"
        elif pos.startswith('TO'):
            return "Может быть частью инфинитива"
        elif pos.startswith('WRB'):
            return "Может быть частью обстоятельства"
        elif pos.startswith('POS'):
            return "Обозначает принадлежность"
        elif pos.startswith('RP'):
            return "Может быть частью фразового глагола"
        elif pos.startswith('FW'):
            return "Иностранное слово, роль зависит от контекста"
        elif pos.startswith('UH'):
            return "Выражает эмоцию или реакцию"
        else:
            return "Роль не определена"
