const { gql } = require('apollo-server');

const typeDefs = gql`
# schema defintions

type Query {
    "Get tracks for home page"
    tracksForHome: [Track!]!
}

"A track is a group of modules that teaches about a specific topic"
type Track {
    "ID of the track"
    id: ID!,
    "Title of the track"
    title: String!
    "Author of the track"
    author: Author!
    thumbnail: String
    "Duration of the track"
    length: Int
    modulesCount: Int
}

"Author of a complete Track or a Module"
type Author {
    "Id of the author"
    id: ID!
    "Name of the author"
    name: String
    photo: String
}
`;


module.exports = typeDefs;
