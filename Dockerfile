# Use official Ruby image
FROM ruby:3.3

# Set working directory
WORKDIR /app

# Copy ruby files
COPY . .

# Expose port
EXPOSE 10000

# Run the Ruby server
CMD ["ruby", "server_.rb"]
