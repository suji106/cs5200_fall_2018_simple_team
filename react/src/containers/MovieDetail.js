import React from 'react';
import GenerateRequest from '../components/GenerateRequest';
import MoviePartners from '../components/MoviePartners';
import PendingRequests from '../components/PendingRequests';
import MovieService from "../services/MovieService";
import RequestService from '../services/RequestService'
import {Link} from 'react-router-dom';
import UserService from "../services/UserService";
import GenerateScreening from "../components/GenerateScreening"
import ScreeningTiles from "../components/ScreeningTiles";
import {Redirect} from 'react-router-dom';
import ReviewsList from "./ReviewList";


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

        if (this.state.typeofUser === "Admin" || this.state.userType === "secretaryAcc") {
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

    criticLogic(criticStatus) {


        switch (criticStatus) {

            case "accepted":

                this.setState({userType: "criticAcc", displayTop: true, displayCom: true})

                return;


            case "rejected":

                this.setState({
                    userType: "criticRej",
                    displayTop: true
                })
                return;


            case "pending": {
                this.setState({
                    userType: "criticPen",
                    displayTop: true
                })
                return;

            }

            default:
                this.setState({
                    userType: "critic",
                    displayTop: true
                });
                return;


        }
    }

    viewerLogic(viewerStatus) {

        switch (viewerStatus) {
            case "accepted":

                this.setState({userType: "viewerAcc", displayTop: true, displayCom: true})

                return;
            case "rejected":

                this.setState({
                    userType: "viewerRej",
                    displayTop: true
                });
                return;


            case "pending":
                this.setState({
                    userType: "viewerPen",
                    displayTop: true
                });
                return;


            default:
                this.setState({
                    userType: "viewer",
                    displayTop: true
                });
                return;


        }


    }

    decideUser() {

        let movieId = this.props.location.state.movie.id;

        let user = this.state.typeofUser;
        console.log(user)
        if (user === "Secretary") {

            this.movieService.ownMovie(movieId).then(res => {

                if (res.isSecretary === "true") {
                    this.setState({
                        displayTop: true,
                        displayCom: true,
                        userType: "secretaryAcc"
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
                this.criticLogic(res.status)
            })
        }

        if (user === "Viewer") {
            this.RequestService.getRequestStatus(movieId).then(res => {
                this.viewerLogic(res.status)
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
                            <h5>Would you like to host a screening?</h5>
                            <GenerateScreening movieId={this.props.location.state.movie.id}/>
                        </div>

                        <div className="app-container">
                            <h5>Upcoming Screenings</h5>
                            <ScreeningTiles movieId={this.props.location.state.movie.id}/>
                        </div>
                    </div>
                );
                break;
            case "viewerRej":
                return (
                    <div>

                        <h6>We are sorry to inform that your request for movie contribution has been rejected</h6>

                    </div>
                );
                break;

            case "viewerPen":
                return (
                    <div>

                        <h6>Your request for contribution is still pending</h6>

                    </div>
                );
                break;

            case "criticRej":
                return (
                    <div>

                        <h6>we are sorry to inform that your request for criticing has been rejected</h6>

                    </div>
                );
                break;

            case "criticPen":
                return (
                    <div>

                        <h6>Your request for criticing is still pending</h6>

                    </div>
                );
                break;

            case "secretaryAcc":
                return (
                    <div>
                        <div className="app-container">
                            <PendingRequests movieId={this.props.location.state.movie.id}/>
                        </div>
                        <div>
                            <h5>Upcoming Screenings</h5>
                            <ScreeningTiles movieId={this.props.location.state.movie.id}/>
                        </div>
                    </div>
                );
                break;

            case "viewer":
                return (
                    <div>
                        <h5>Would you like to contribute to this movie?</h5>
                        <GenerateRequest movieId={this.props.location.state.movie.id}/>
                    </div>
                );
                break;

            case "critic":
                return (
                    <div className="app-container">
                        <h5>Would you like to critic this movie?</h5>
                        <GenerateRequest movieId={this.props.location.state.movie.id}/>
                    </div>
                );
                break;

            case "criticAcc":
                return (
                    <div>
                        <div className="app-container">
                            <h5>Would you like to host a screening?</h5>
                            <GenerateScreening movieId={this.props.location.state.movie.id}/>
                        </div>
                        <div className="app-container">
                            <h3>Upcoming Screenings</h3>
                            <ScreeningTiles movieId={this.props.location.state.movie.id}/>
                        </div>
                    </div>
                );
                break;

            case "viewerAcc":
                return (
                    <div className="app-container">
                        <h5>Upcoming Screenings</h5>
                        <ScreeningTiles movieId={this.props.location.state.movie.id}/>
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
                    {this.state.displayCom && <ReviewsList movieId={this.props.location.state.movie.id}/>}


                </div>
            </div>

        )
    }
}

export default MovieDetail;
