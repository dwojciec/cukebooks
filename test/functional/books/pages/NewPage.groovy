package books.pages

import data.Data
import geb.Page

class NewPage extends Page {
    static at = {
        title ==~ /Create Book/
    }

    static content = {
        author { $("form").author() }
        btitle { $("form").title() }
        save { $('input.save') }
    }

    def add(String bookTitle) {
        def book = Data.findByTitle(bookTitle)

        if (book.title == bookTitle) {
            author = book.author
        }
        btitle = bookTitle

        save.click()
    }
}