const express = require('express');
const path = require('path');
const app = express();
app.use(express.json());

// Serve static files
app.use(express.static(path.join(__dirname, 'public')));

// Movie array
let movies = [
  { title: 'The Matrix', genre: 'Sci-Fi', year: 1999, director: 'The Wachowskis' },
  { title: 'Inception', genre: 'Sci-Fi', year: 2010, director: 'Christopher Nolan' },
  { title: 'The Godfather', genre: 'Drama', year: 1972, director: 'Francis Ford Coppola' },
  { title: 'Pulp Fiction', genre: 'Crime', year: 1994, director: 'Quentin Tarantino' },
  { title: 'The Dark Knight', genre: 'Action', year: 2008, director: 'Christopher Nolan' }
];

// Home route
app.get('/', (req, res) => {
  res.sendFile(path.join(__dirname, 'public', 'index.html'));
});

// 1. GET /api/movies - Retrieve all movies
app.get('/api/movies', (req, res) => {
  res.json(movies);
});

// 2. GET /api/movies/filter?genre=[genre name] - Filter movies by genre
app.get('/api/movies/filter', (req, res) => {
  const genre = req.query.genre;
  if (!genre) {
    return res.status(400).json({ error: 'Genre query parameter is required' });
  }

  const filtered = movies.filter(movie => movie.genre.toLowerCase() === genre.toLowerCase());
  res.json(filtered);
});

// 3. GET /api/movies/:id - Get movie by index
app.get('/api/movies/:id', (req, res) => {
  const id = parseInt(req.params.id, 10);
  if (isNaN(id) || id < 0 || id >= movies.length) {
    return res.status(404).json({ error: 'Movie not found' });
  }
  res.json(movies[id]);
});

// 4. POST /api/movies - Add a new movie
app.post('/api/movies', (req, res) => {
  const { title, genre, year, director } = req.body;
  if (!title || !genre || !year || !director) {
    return res.status(400).json({ error: 'Missing required movie fields' });
  }
  if (typeof year !== 'number') {
    return res.status(400).json({ error: 'Year must be a number' });
  }

  const newMovie = { title, genre, year, director };
  movies.push(newMovie);
  res.status(201).json(newMovie);
});

// 5. PUT /api/movies/:id - Update movie by index
app.put('/api/movies/:id', (req, res) => {
  const id = parseInt(req.params.id, 10);
  const { title, genre, year, director } = req.body;

  if (isNaN(id) || id < 0 || id >= movies.length) {
    return res.status(404).json({ error: 'Movie not found' });
  }

  if (!title || !genre || !year || !director) {
    return res.status(400).json({ error: 'Missing required movie fields' });
  }
  if (typeof year !== 'number') {
    return res.status(400).json({ error: 'Year must be a number' });
  }

  movies[id] = { title, genre, year, director };
  res.json(movies[id]);
});

// 6. DELETE /api/movies/:id - Delete movie by index
app.delete('/api/movies/:id', (req, res) => {
  const id = parseInt(req.params.id, 10);
  if (isNaN(id) || id < 0 || id >= movies.length) {
    return res.status(404).json({ error: 'Movie not found' });
  }

  const removedMovie = movies.splice(id, 1);
  res.json({ message: 'Movie deleted', movie: removedMovie[0] });
});

// Start the server
const PORT = 3000;
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});