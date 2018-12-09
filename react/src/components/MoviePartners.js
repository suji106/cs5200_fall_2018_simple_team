import React from 'react';
import { Link } from 'react-router-dom';
import RequestService from "../services/RequestService";
import UserService from "../services/UserService";


export default class MoviePartners
    extends React.Component {
    constructor(props) {
        super(props);
        this.delete=this.delete.bind(this);
        this.state={requests:[],admin:false};
        this.RequestService=RequestService.instance;
        this.updateRequests = this.updateRequests.bind(this);
        this.updateLogin = this.updateLogin.bind(this);
        this.UserService = UserService.instance;


    }
    updateLogin(){

        this.UserService.getUserType().then(res => {
            if (res.userType === "Admin") {
                this.setState({admin: true})
            }})

    }

    delete(requestId){
        if(!window.confirm("are you sure, you want to delete?"))
        {return;}
    this.RequestService.deleteRequest(requestId)
        .then(()=>{this.updateRequests()});
    }

    updateRequests(){
        this.props.getMembers(this.props.movieId).then(requests => this.setState({requests:requests}));

    }
    componentDidMount(){
this.updateRequests();
this.updateLogin();

    }
    render() {
        return (
            <div className="row app-container1">
                {this.state.requests.map(request => (
                    <div className="col-sm-3 ">
                        <div className="card  bg-light" >


                        <div className="card-body">
                            <Link to={'/profile/'+request.user.id}>{request.user.name}</Link>
                            <p className="card-text">{request.message}</p>

                            {this.state.admin && <button className="btn btn-danger" onClick={() => {this.delete(request.id)}}>Delete</button>}
                        </div>
                    </div>
                    </div>

                ))}
            </div>

        );
    }




}
