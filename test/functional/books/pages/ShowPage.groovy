package books.pages

import geb.Page
import data.Data

class ShowPage extends Page {
    static at = {
        title ==~ /Show Book/
    }

    static content = {
        row { val ->
            $('span.property-label', text: val).parent()
        }

        value { val ->
            row(val).find('span.property-value').text()
        }

        btitle {
            value('Title')
        }

        author {
            value('Author')
        }
    }

    def check(String bookTitle) {
        def book = Data.findByTitle(bookTitle)

        assert book.author == author
        assert book.title == btitle
    }
}