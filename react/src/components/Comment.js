import React from 'react';
import UserService from "../services/UserService";
import CommentService from "../services/CommentService";

class Comment extends React.Component {

    constructor(props) {
        super(props);
        this.state={admin:false};
        this.updateLogin = this.updateLogin.bind(this);
        this.delete = this.delete.bind(this);
        this.UserService = UserService.instance;
        this.CommentService = CommentService.instance;
    }

    componentDidMount(){
        this.updateLogin();
}

    updateLogin(){

        this.UserService.getUserType().then(res => {
            if (res.userType === "Admin") {
                this.setState({admin: true})
            }})

    }

    delete(commentId){
        if(!window.confirm("are you sure, you want to delete?"))
        {return;}
        this.CommentService.deleteComment(commentId)
            .then(()=>{this.props.updateComments()});
    }

    render() {


        return (

                <div className="commentBox">

                    {this.state.admin && <button className="btn btn-danger float-right" onClick={() => {this.delete(this.props.commentObject.id)}}>Delete</button>}
                    <div >

                                        {this.props.commentObject.comment}

                    </div>
                    <h6>
                        {this.props.commentObject.user.name} &nbsp;({this.props.commentObject.userType}) &nbsp; on {new Date(this.props.commentObject.created).toLocaleString()}
                    </h6>

                </div>

        )
    }
}

export default Comment;