import React from 'react';
import CommentService from "../services/CommentService";
import Comment from "../components/Comment"

class CommentsList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            comments: [],
            created: '',
            movieId:this.props.movieId,
            comment: ''
        }
        this.commentService = CommentService.instance;
        this.updateComments= this.updateComments.bind(this);
        this.createComment = this.createComment.bind(this);
        this.componentDidMount = this.componentDidMount.bind(this);
    }

    componentDidMount() {
this.updateComments();
    }



 updateComments(){
     var commentUrl = "https://moviewalk.herokuapp.com/api/PID/movie/comments"
         .replace('PID', this.state.movieId);
     fetch(commentUrl).then(response => (response.json()))
         .then(comments => {
             console.log(comments)
             this.setState({
                 comments: comments
             })
         });
 }

    createComment() {
        this.commentService.createComment(this.state.comment, this.state.movieId).then(()=> {this.updateComments()});
    }

    render() {

        return (
            <div className="app-container">
                <h5>
                    Comments
                </h5>
                <div>
                    {this.state.comments.map(comment => (
                        <Comment commentObject={comment} updateComments = {this.updateComments}/>
                    ))}
                </div>


                    <div className="input-group ">

                        <textarea onChange={event => {
                            this.setState({
                                comment: event.target.value
                            })
                        }} className="form-control" aria-label="With textarea"> </textarea>
                        <div className="input-group-append">
                        <button className="btn btn-primary"
                                onClick={this.createComment}>
                            Post Comment
                        </button>
                        </div>
                    </div>





            </div>
        )
    }
}

export default CommentsList;