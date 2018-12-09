let _singleton = Symbol();
const MOVIE_API_URL =
    'https://moviewalk.herokuapp.com/api/movies';

class MovieService {
    constructor(singletonToken) {
        if (_singleton !== singletonToken)
            throw new Error('Cannot instantiate directly.');
    }

    static get instance() {
        if (!this[_singleton])
            this[_singleton] = new MovieService(_singleton);
        return this[_singleton]
    }

    findAllMovies() {
        return fetch(MOVIE_API_URL).then((response) => {
            return response.json();
        })
    }

    addMovie(movie) {
        return fetch("https://moviewalk.herokuapp.com/api/movie", {
            body: JSON.stringify(movie),
            headers: {
                'Content-Type': 'application/json'
            }, credentials: 'include',
            method: 'POST'
        }).then(function (response) {
            return response.json();
        })
    }


    ownMovie(movieId) {
        return fetch('https://moviewalk.herokuapp.com/api/movie/' + movieId + '/owner', {
            credentials: 'include'
        }).then(response => response.json());
    }

    getOwnedMovies(movieId) {
        return fetch('https://moviewalk.herokuapp.com/api/movies/owner', {
            credentials: 'include'
        }).then(response => response.json());
    }

    deleteMovie(movieId) {
        return fetch('https://moviewalk.herokuapp.com/api/movie/' + movieId, {
            method: 'delete',
            credentials: 'include'
        });
    }
}

export default MovieService;
