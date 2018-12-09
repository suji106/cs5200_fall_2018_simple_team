import React from 'react';
import { Link } from 'react-router-dom';
import MeetingService from "../services/MeetingService";
import UserService from "../services/UserService";


export default class MeetingTiles
    extends React.Component {
    constructor(props) {
        super(props);
        this.state={meetings:[],admin:false};
        this.updateLogin = this.updateLogin.bind(this);
        this.MeetingService = MeetingService.instance;
        this.delete = this.delete.bind(this);
        this.UserService = UserService.instance;
        this.updateMeetings = this.updateMeetings.bind(this);
    }


    updateLogin(){

        this.UserService.getUserType().then(res => {
            if (res.userType === "Admin") {
                this.setState({admin: true})
            }})

    }
    delete(meetingId){
        if(!window.confirm("are you sure, you want to delete?"))
        {return;}
        this.MeetingService.deleteMeeting(meetingId)
            .then(()=>{this.updateMeetings()});
    }

    updateMeetings(){

        this.MeetingService.getMeetings(this.props.movieId).then(meetings => this.setState({meetings:meetings}));
    }

    componentDidMount(){

        this.updateMeetings();
        this.updateLogin();
    }
    render() {
        return (
            <div className="row app-container1">
                {this.state.meetings.map(meeting => (

                    <div className="col-sm-3 ">
                    <div className="card  bg-light" >

                        <div className="card-body">
                            <h2 className="card-title">{meeting.title}</h2>
                            <p className="card-text">{meeting.message}</p>
                            <p className="card-text">{meeting.location}</p>
                            <p className="card-text">{new Date(meeting.time).toLocaleString()}</p>
                            <Link to={'/profile/${this.request.user.id}'}></Link>
                            {this.state.admin && <button className="btn btn-danger" onClick={() => {this.delete(meeting.id)}}>Delete</button>}
                        </div>
                    </div>
                    </div>

                ))}
            </div>

        );
    }





}
