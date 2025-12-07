# server.rb
require 'socket'
port = ENV.fetch("PORT", 5678).to_i
server = TCPServer.new port
puts "Running on PORT #{port}"
STDOUT.flush

while session = server.accept
  begin
    request = session.gets
    puts request
    STDOUT.flush
    
    # Handle nil request
    next if request.nil?
    
    method, path, http_version = request.split(" ")
    
    session.print "HTTP/1.1 200 OK\r\n"
    session.print "Content-Type: text/html\r\n"
    session.print "\r\n"
    
    # Do NOT send a body for HEAD request
    if method != "HEAD"
      session.print <<~HTML
        <h1>Hi Ruby World!</h1>
      HTML
    end
  rescue Errno::EPIPE, Errno::ECONNRESET, IOError => e
    # Client disconnected, ignore
    puts "Client disconnected: #{e.message}"
    STDOUT.flush
  ensure
    session.close rescue nil
  end
end