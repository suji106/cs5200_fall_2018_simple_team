import React from 'react';
import GenerateRequest from '../components/GenerateRequest';
import MoviePartners from '../components/MoviePartners';
import PendingRequests from '../components/PendingRequests';
import MovieService from "../services/MovieService";
import RequestService from '../services/RequestService'
import {Link} from 'react-router-dom';
import UserService from "../services/UserService";
import GenerateMeeting from "../components/GenerateMeeting"
import MeetingTiles from "../components/MeetingTiles";
import {Redirect} from 'react-router-dom';
import CommentsList from "./CommentList";


class MovieDetail extends React.Component {
    constructor(props) {
        super(props);
        this.RequestService = RequestService.instance;
        this.movieService = MovieService.instance;
        this.UserService = UserService.instance;
        this.decideUser = this.decideUser.bind(this);
        this.headerJSX = this.headerJSX.bind(this);
        this.logout = this.logout.bind(this);
        this.showDelete = this.showDelete.bind(this);
        this.delete = this.delete.bind(this);

        this.state = {displayCom: false, userType: "", displayTop: false, typeofUser: "", redirect: false}
    }


    logout() {
        this.UserService.logout().then(res => {
                this.setState({redirect: true});
            }
        );

    }

    delete(movieId) {

        if (!window.confirm("are you sure, you want to delete?")) {
            return;
        }
        this.movieService.deleteMovie(movieId)
            .then(() => {
                this.setState({redirect: true})
            });
    }

    showDelete() {

        if (this.state.typeofUser === "Admin" || this.state.userType === "ownerAcc") {
            return true;
        }
        return false;

    }

    componentDidMount() {

        this.UserService.getUserType().then(res => {
            if (res !== "None") {
                this.setState({typeofUser: res.userType})
            }
            this.decideUser();
        });


    }

    mentorLogic(mentorStatus) {


        switch (mentorStatus) {

            case "accepted":

                this.setState({userType: "mentorAcc", displayTop: true, displayCom: true})

                return;


            case "rejected":

                this.setState({
                    userType: "mentorRej",
                    displayTop: true
                })
                return;


            case "pending": {
                this.setState({
                    userType: "mentorPen",
                    displayTop: true
                })
                return;

            }

            default:
                this.setState({
                    userType: "mentor",
                    displayTop: true
                });
                return;


        }
    }

    contributorLogic(contributorStatus) {

        switch (contributorStatus) {
            case "accepted":

                this.setState({userType: "contributorAcc", displayTop: true, displayCom: true})

                return;
            case "rejected":

                this.setState({
                    userType: "contributorRej",
                    displayTop: true
                });
                return;


            case "pending":
                this.setState({
                    userType: "contributorPen",
                    displayTop: true
                });
                return;


            default:
                this.setState({
                    userType: "contributor",
                    displayTop: true
                });
                return;


        }


    }

    decideUser() {

        let movieId = this.props.location.state.movie.id;

        let user = this.state.typeofUser;
        console.log(user)
        if (user === "Owner") {

            this.movieService.ownMovie(movieId).then(res => {

                if (res.isOwner === "true") {
                    this.setState({
                        displayTop: true,
                        displayCom: true,
                        userType: "ownerAcc"
                    })
                }
                ;

            })

        }
        if (user === "Admin") {

            this.setState({
                userType: "Admin", displayTop: true,
                displayCom: true,
            })
        }

        if (user === "Critic") {

            this.RequestService.getRequestStatus(movieId).then(res => {
                this.mentorLogic(res.status)
            })
        }

        if (user === "Viewer") {
            this.RequestService.getRequestStatus(movieId).then(res => {
                this.contributorLogic(res.status)
            })

        }
    }

    headerJSX() {

        switch (this.state.userType) {


            case "Admin":
                return (
                    <div>
                        <div className="app-container">
                            <PendingRequests movieId={this.props.location.state.movie.id}/>
                        </div>

                        <div className="app-container">
                            <h5>Would you like to host a meeting?</h5>
                            <GenerateMeeting movieId={this.props.location.state.movie.id}/>
                        </div>

                        <div className="app-container">
                            <h5>Upcoming Meetings</h5>
                            <MeetingTiles movieId={this.props.location.state.movie.id}/>
                        </div>
                    </div>
                );
                break;
            case "contributorRej":
                return (
                    <div>

                        <h6>We are sorry to inform that your request for movie contribution has been rejected</h6>

                    </div>
                );
                break;

            case "contributorPen":
                return (
                    <div>

                        <h6>Your request for contribution is still pending</h6>

                    </div>
                );
                break;

            case "mentorRej":
                return (
                    <div>

                        <h6>we are sorry to inform that your request for mentoring has been rejected</h6>

                    </div>
                );
                break;

            case "mentorPen":
                return (
                    <div>

                        <h6>Your request for mentoring is still pending</h6>

                    </div>
                );
                break;

            case "ownerAcc":
                return (
                    <div>
                        <div className="app-container">
                            <PendingRequests movieId={this.props.location.state.movie.id}/>
                        </div>
                        <div>
                            <h5>Upcoming Meetings</h5>
                            <MeetingTiles movieId={this.props.location.state.movie.id}/>
                        </div>
                    </div>
                );
                break;

            case "contributor":
                return (
                    <div>
                        <h5>Would you like to contribute to this movie?</h5>
                        <GenerateRequest movieId={this.props.location.state.movie.id}/>
                    </div>
                );
                break;

            case "mentor":
                return (
                    <div className="app-container">
                        <h5>Would you like to mentor this movie?</h5>
                        <GenerateRequest movieId={this.props.location.state.movie.id}/>
                    </div>
                );
                break;

            case "mentorAcc":
                return (
                    <div>
                        <div className="app-container">
                            <h5>Would you like to host a meeting?</h5>
                            <GenerateMeeting movieId={this.props.location.state.movie.id}/>
                        </div>
                        <div className="app-container">
                            <h3>Upcoming Meetings</h3>
                            <MeetingTiles movieId={this.props.location.state.movie.id}/>
                        </div>
                    </div>
                );
                break;

            case "contributorAcc":
                return (
                    <div className="app-container">
                        <h5>Upcoming Meetings</h5>
                        <MeetingTiles movieId={this.props.location.state.movie.id}/>
                    </div>
                );
                break;

        }

    }


    render() {
        if (this.state.redirect === true) {
            return <Redirect to='/'/>
        }

        return (
            <div>


                <nav className="navbar navbar-expand-lg navbar-dark navbar-fixed-top">
                    <a className="navbar-brand" href="/">Simple Screens</a>
                    <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav"
                            aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                        <span className="navbar-toggler-icon"></span>
                    </button>
                    <div className="collapse navbar-collapse" id="navbarNav">
                        <ul className="navbar-nav">
                            <li className="nav-item">
                                <Link className="nav-link" to='/'>Home</Link>
                            </li>
                            <li className="nav-item">
                                <Link className="nav-link" to='/movies'>My Movies</Link>
                            </li>

                            <li className="nav-item">
                                <Link className="nav-link" to='/connections'>My Connections</Link>
                            </li>

                            <li className="nav-item">
                                <a className="nav-link" onClick={this.logout}>Logout</a>
                            </li>

                        </ul>
                    </div>
                </nav>

                <div className="container-fluid app-container">

                    {this.showDelete() && <button className="btn btn-danger float-right movie-delete" onClick={() => {
                        this.delete(this.props.location.state.movie.id)
                    }}>Delete Movie</button>}
                    <h3 className="movieTitle"><a
                        href={this.props.location.state.movie.url}>{this.props.location.state.movie.title}</a></h3>
                    {this.state.displayTop && this.headerJSX()}
                    <h5>Movie Description</h5>
                    <p>{this.props.location.state.movie.summary}</p>

                    <h5>Movie Languages</h5>
                    <ul>
                        {this.props.location.state.movie.languages.split(" ").map(language => (<li> {language}</li>))}
                    </ul>
                    <div className="app-container">
                        <h5>Critics</h5>
                        <MoviePartners movieId={this.props.location.state.movie.id}
                                       getMembers={this.RequestService.getCritics}/>
                    </div>
                    <div className="app-container">
                        <h5>Viewers</h5>
                        <MoviePartners movieId={this.props.location.state.movie.id}
                                       getMembers={this.RequestService.getViewers}/>
                    </div>
                    {this.state.displayCom && <CommentsList movieId={this.props.location.state.movie.id}/>}


                </div>
            </div>

        )
    }
}

export default MovieDetail;
