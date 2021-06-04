using DataFrames, Plots, CSV

plotName = "Model_0.png"
dataName = "DataSEIR.csv"
save = true
cumulativeIE = false
partToPlot = (0.0, 1.0)

SIRframe = DataFrame(CSV.File("./data/$dataName"))
len = size(SIRframe)[1]
partRange = (Int(round(len * partToPlot[1])) + 1):(Int(round(len * partToPlot[2])))
plotFrame = SIRframe[partRange, :]

SIRplot = plot(
    plotFrame[!, :Day], 
    plotFrame[!, :Suspectible],
    title = "SEIR Model Evolution",
    xlabel = "Day",
    ylabel = "Cases",
    label = "Suspectible",
    color = 1
)
if cumulativeIE
    plot!(
        SIRplot,
        plotFrame[!, :Exposed] + plotFrame[!, :Infectious],
        label = "Infectious",
        color = :red
    )
else
    plot!(
        SIRplot,
        plotFrame[!, :Exposed],
        label = "Exposed",
        color = 2
    )
    plot!(
        SIRplot,
        plotFrame[!, :Infectious],
        label = "Infectious",
        color = :red
    )
end
plot!(
    SIRplot,
    plotFrame[!, :Recovered],
    label = "Recovered",
    color = :green
)
plot!(
    SIRplot,
    plotFrame[!, :Dead],
    label = "Dead",
    color = :Black
)

display(SIRplot)

if save
    savefig(SIRplot, "./savedPlots/$(plotName)")
end
