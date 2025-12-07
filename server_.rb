# server.rb
require 'socket'

port = ENV.fetch("PORT", 5678).to_i
server = TCPServer.new port

puts "Running on PORT #{port}"

while session = server.accept
  request = session.gets
  puts request

  method, path, http_version = request.split(" ")

  session.print "HTTP/1.1 200 OK\r\n"
  session.print "Content-Type: text/html\r\n"
  session.print "\r\n"

  # Do NOT send a body for HEAD request → Render health check
  if method != "HEAD"
    session.print <<~HTML
      <h1>Hi Ruby World!</h1>
    HTML
  end

  session.close
end
