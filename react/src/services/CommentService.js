let _singleton = Symbol();
const COMMENT__URL =
    'https://moviewalk.herokuapp.com/api/PID/comment';

class CommentService {
    constructor(singletonToken) {
        if (_singleton !== singletonToken)
            throw new Error('Cannot instantiate directly.');
    }

    createComment(comment, movieId) {

        return fetch(COMMENT__URL.replace('PID', movieId),
            {
                body: JSON.stringify({comment: comment}),
                headers: {
                    'Content-Type': 'application/json'
                },
                method: 'post',
                credentials: 'include'
            }
        ).then(res =>  res.json());
    }

    deleteComment(commentID){

        return fetch('https://moviewalk.herokuapp.com/api/comment/'+commentID,{
            method:'delete',
            credentials: 'include'});
    }


    static get instance() {
        if (!this[_singleton])
            this[_singleton] = new CommentService(_singleton);
        return this[_singleton]
    }


}

export default CommentService;