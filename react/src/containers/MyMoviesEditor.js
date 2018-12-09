import React from 'react';
import MovieTile from "../components/MovieTile";
import MovieService from "../services/MovieService";
import UserService from "../services/UserService";
import SecretaryMovies from "./SecretaryMovies"
import MyMovies from "./MyMovies"
import {Link} from 'react-router-dom';
import { Redirect } from 'react-router-dom';


class MyMoviesEditor extends React.Component {
    constructor() {
        super();
        //this.MovieService = MovieService.instance;
        this.UserService=UserService.instance;
        this.decideMyMovies = this.decideMyMovies.bind(this);
        this.logout = this.logout.bind(this);
        this.state ={
            userType:"",
            decide:false,
            loggedOut:false

        };
    }


    componentDidMount() {



        this.UserService.getUserType().then(res => {

                if (res.userType !== "None") {
                    this.setState({userType: res.userType, decide:true})
                }}
            );

    }



    logout(){
        this.UserService.logout().then(res => {
            this.setState({loggedOut:true});}
        );

    }


decideMyMovies(){

        let user = this.state.userType;
        if(user==="Secretary"){

            return <SecretaryMovies />
        }

        else{
            return <MyMovies />
        }

}



    render() {
        if (this.state.loggedOut === true) {
            return <Redirect to='/' />
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
                                <Link className="nav-link" to='/connections'>My Connections</Link>
                            </li>
                            <li className="nav-item">
                                <a className="nav-link" onClick={this.logout}>Logout</a>
                            </li>
                        </ul>
                    </div>
                </nav>
                {this.state.decide && this.decideMyMovies()}
            </div>

        )
    }
}
export default MyMoviesEditor;
