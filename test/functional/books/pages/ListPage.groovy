package books.pages

import data.Data
import geb.Page

class ListPage extends Page {
    static url = "book/index"

    static at = {
        title ==~ /Book List/
    }

    static content = {
        create (to: NewPage) { $('a.create') }

        list {
            $('table tbody')
        }

        row { int i ->
            list.find('tr', i)
        }

        btitle { int i ->
            row(i).find('td', 1).text()
        }

        author { int i ->
            row(i).find('td', 0).text()
        }
    }

    def toNewPage () {
        create.click ()
    }

    def check(String bookTitle, int row) {
        def book = Data.findByTitle(bookTitle)

        assert book.author == author(row)
        assert book.title == btitle(row)
    }
}
